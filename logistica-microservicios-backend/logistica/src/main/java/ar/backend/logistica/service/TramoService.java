package ar.backend.logistica.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ar.backend.logistica.client.CamionClient;
import ar.backend.logistica.client.TransportistaClient;
import ar.backend.logistica.dto.CamionDTO;
import ar.backend.logistica.dto.TramoAsignadoResponseDTO;
import ar.backend.logistica.dto.TramoCamionResponseDTO;
import ar.backend.logistica.dto.TramoConFechaFinReal;
import ar.backend.logistica.dto.TramoConFechaInicioReal;
import ar.backend.logistica.dto.TramoTransportistaResponseDTO;
import ar.backend.logistica.dto.TramosAsignadosResponseDTO;
import ar.backend.logistica.dto.TransportistaDTO;
import ar.backend.logistica.dto.UbicacionSimpleDTO;
import ar.backend.logistica.models.Contenedor;
import ar.backend.logistica.models.Deposito;
import ar.backend.logistica.models.Ruta;
import ar.backend.logistica.models.Solicitud;
import ar.backend.logistica.models.Tramo;
import ar.backend.logistica.models.enums.EstadoContenedor;
import ar.backend.logistica.models.enums.EstadoSolicitud;
import ar.backend.logistica.models.enums.EstadoTramo;
import ar.backend.logistica.repository.ContenedorRepository;
import ar.backend.logistica.repository.SolicitudRepository;
import ar.backend.logistica.repository.TramoRepository;
import ar.backend.logistica.repository.DepositoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Importar Lombok

import java.time.Duration;

@Slf4j // Habilitar logs
@Service
@RequiredArgsConstructor
public class TramoService {
    private final TramoRepository tramoRepository;
    private final SolicitudRepository solicitudRepository;
    private final ContenedorRepository contenedorRepository;
    private final CamionClient camionClient;
    private final DepositoRepository depositoRepository;
    private final TransportistaClient transportistaClient;
    
    @Transactional
    public TramoConFechaInicioReal iniciarTramo(int idTramo) {
        log.info("[SERVICIO] [INICIAR_TRAMO] Procesando inicio para TramoID: {}", idTramo);

        Tramo tramo = tramoRepository.findById(idTramo)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));

        // Validar que el tramo tenga asignado un camión y un transportista
        if (tramo.getIdTransportista() == null) {
            log.error("[SERVICIO] [ERROR] El TramoID {} no tiene chofer asignado.", idTramo);
            throw new RuntimeException("El tramo no tiene transportista asignado.");
        } else if (tramo.getPatenteCamion() == null) {
            log.error("[SERVICIO] [ERROR] El TramoID {} no tiene camión asignado.", idTramo);
            throw new RuntimeException("El tramo no tiene camión asignado.");
        } else {
            tramo.setFechaHoraInicioReal(LocalDateTime.now());
            tramo.setEstado(EstadoTramo.EN_TRANSITO);
            tramoRepository.save(tramo);
            
            // actualizar contenedor y solicitud a EN_TRANSITO
            Solicitud solicitud = tramo.getRuta().getSolicitud();
            Contenedor contenedor = solicitud.getContenedor();
            
            log.info("[SERVICIO] [INICIAR_TRAMO] Actualizando estados. SolicitudID: {}, ContenedorID: {} -> EN_TRANSITO", 
                     solicitud.getIdSolicitud(), contenedor.getIdContenedor());

            solicitud.setEstado(EstadoSolicitud.EN_TRANSITO);
            contenedor.setEstado(EstadoContenedor.EN_TRANSITO);

            // si se inicia un tramo que no es el inicial quiere decir que está saliendo de un depósito
            if (tramo.getNumeroOrden() != 1) {
                log.info("[SERVICIO] [INICIAR_TRAMO] Salida de depósito detectada. Removiendo contenedor del inventario.");
                contenedor.setDeposito(null);
                Deposito deposito = depositoRepository.findByIdUbicacion(tramo.getUbicacionOrigen().getIdUbicacion())
                        .orElseThrow(() -> new RuntimeException("El deposito no se encuentra registrado"));
                deposito.deleteContenedor(contenedor);
                depositoRepository.save(deposito);
            }

            // guardamos los cambios
            solicitudRepository.save(solicitud);
            contenedorRepository.save(contenedor);
        }

        log.info("[SERVICIO] [INICIAR_TRAMO] Inicio registrado correctamente a las: {}", tramo.getFechaHoraInicioReal());

        return new TramoConFechaInicioReal(
            tramo.getIdTramo(),
            tramo.getNumeroOrden(),
            tramo.getUbicacionOrigen().getIdUbicacion(),
            tramo.getUbicacionDestino().getIdUbicacion(),
            tramo.getTipoTramo(),
            tramo.getDistanciaEstimada(),
            tramo.getFechaHoraInicioReal(),
            tramo.getEstado().name()
        );
    }

    // ---------------------- FINALIZAR TRAMO ------------------------------------------------
    @Transactional
    public TramoConFechaFinReal finalizarTramo(int idTramo) {
        log.info("[SERVICIO] [FINALIZAR_TRAMO] Procesando fin para TramoID: {}", idTramo);

        Tramo tramo = tramoRepository.findById(idTramo)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));

        tramo.setFechaHoraFinReal(LocalDateTime.now());
        tramo.setEstado(EstadoTramo.FINALIZADO);
        tramoRepository.save(tramo);

        // Si el tramo no llega al destino final, el contenedor pasa al estado EN_DEPOSITO
        // Si el tramo llega al destino final, el contenedor pasa al estado ENTREGADO y la solicitud al estado ENTREGADA
        Solicitud solicitud = tramo.getRuta().getSolicitud();
        Contenedor contenedor = solicitud.getContenedor();

        boolean esTramoFinal =
            tramoRepository.countByRutaIdRutaAndNumeroOrdenGreaterThan(
                tramo.getRuta().getIdRuta(),
                tramo.getNumeroOrden()
            ) == 0;

        Double costoFinal = null;

        if (esTramoFinal) {
            log.info("[SERVICIO] [FINALIZAR_TRAMO] Es el último tramo. Solicitud ENTREGADA.");
            contenedor.setEstado(EstadoContenedor.ENTREGADO);
            solicitud.setEstado(EstadoSolicitud.ENTREGADA);
            costoFinal = calcularCostoFinal(solicitud);
            log.info("[SERVICIO] [COSTO_FINAL] Costo total calculado: ${}", costoFinal);
        } else {
            log.info("[SERVICIO] [FINALIZAR_TRAMO] Tramo intermedio. Ingresando contenedor a DEPÓSITO.");
            contenedor.setEstado(EstadoContenedor.EN_DEPOSITO);
            Deposito deposito = depositoRepository.findByIdUbicacion(tramo.getUbicacionDestino().getIdUbicacion())
                    .orElseThrow(() -> new RuntimeException("El deposito no se encuentra registrado"));
            contenedor.setDeposito(deposito);
            deposito.addContenedor(contenedor);
            depositoRepository.save(deposito);
        }
        contenedorRepository.save(contenedor);
        solicitudRepository.save(solicitud);

        String patenteCamion = tramo.getPatenteCamion();

        log.info("[SERVICIO] [CLIENT_FLOTA] Liberando recursos: Camión {} y TransportistaID {}.", patenteCamion, tramo.getIdTransportista());

        camionClient.cambiarDisponibilidadCamion(patenteCamion, "DISPONIBLE");

        transportistaClient.desasignarTramoATransportista(tramo.getIdTransportista(), idTramo);

        return new TramoConFechaFinReal(
            tramo.getIdTramo(),
            tramo.getNumeroOrden(),
            tramo.getUbicacionOrigen().getIdUbicacion(),
            tramo.getUbicacionDestino().getIdUbicacion(),
            tramo.getTipoTramo(),
            tramo.getDistanciaEstimada(),
            tramo.getFechaHoraInicioReal(),
            tramo.getFechaHoraFinReal(),
            tramo.getEstado().name(),
            costoFinal
        );
    }

    public double calcularCostoFinal(Solicitud solicitud) {
        log.info("[SERVICIO] [CALCULAR_COSTO] Iniciando cálculo final para SolicitudID: {}", solicitud.getIdSolicitud());

        // PRIMER VALOR
        double distanciaTotal = solicitud.getDistanciaTotal();

        double volumenContenedor = solicitud.getContenedor().getVolumen();

        // estructura ficticia: costo base por km según volumen
        Map<Double, Double> costoBasePorVolumen = new TreeMap<>();
                costoBasePorVolumen.put(10.0, 50.0);
                costoBasePorVolumen.put(20.0, 80.0);
                costoBasePorVolumen.put(30.0, 120.0);

        // obtengo el costo por km según volumen (si no existe, usar el más grande)
        double costoBaseKm = costoBasePorVolumen.entrySet().stream()
                .filter(e -> volumenContenedor <= e.getKey())
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(150.0);

        double costoBaseXVolumenContenedor = costoBaseKm * distanciaTotal;

        // SEGUNDO + CUARTO VALOR, combinados

        Ruta ruta = solicitud.getRutaAsignada();

        if (ruta == null) {
            throw new RuntimeException("La solicitud no tiene una ruta asignada");
        }   

        List<Tramo> tramos = ruta.getTramos();

        double precioCombustible = solicitud.getParametroTarifa().getPrecioCombustible();

        double costoCombustible = 0;              // segundo valor
        double costoCamionBase = 0;              // cuarto valor

        for (Tramo tramo : tramos) {

            // 1. Obtener camión asignado
            CamionDTO camion = camionClient.obtenerCamionPorPatente(tramo.getPatenteCamion());
            if (camion == null) {
                log.error("[SERVICIO] [ERROR] TramoID {} no tiene camión asignado al calcular costo.", tramo.getIdTramo());
                throw new RuntimeException("El tramo " + tramo.getIdTramo() + " no tiene camión asignado");
            }

            double distancia = tramo.getDistanciaEstimada();

            // =============================
            //   SEGUNDO VALOR (combustible)
            // =============================
            double consumo = camion.getConsumoCombustibleXKilometro();
            double costoTramoCombustible = distancia * consumo * precioCombustible;
            costoCombustible += costoTramoCombustible;

            // =============================
            //   CUARTO VALOR (volumen)
            // =============================

            double volumenCamion = camion.getCapacidadVolumen(); 

            Map<Double, Double> costoBasePorCapacidadVolumen = Map.of(
                10.0, 50.0,   // camión chico  → $50/km
                20.0, 80.0,   // camión mediano→ $80/km
                25.0, 100.0,  // camión mediano-grande → $100/km
                30.0, 120.0   // camión grande → $120/km
            );
            // obtener costo base por km según volumen del camión
            double costoBaseXKm = costoBasePorCapacidadVolumen.entrySet().stream()
                    .filter(entry -> volumenCamion <= entry.getKey())
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse(150.0); // camión muy grande → costo máximo

            // costo para este tramo
            double costoBaseTramo = costoBaseXKm * distancia;

            costoCamionBase += costoBaseTramo;
        }

        // TERCER VALOR
        double costoDepositos = 0;

        // 1. Obtener depósitos
        List<Deposito> depositos = depositoRepository.findAll();
        Set<Integer> idsDepositos = depositos.stream()
                .map(Deposito::getIdUbicacion)
                .collect(Collectors.toSet());

        // 2. Recorrer los tramos (tramo i -> i+1)
        for (int i = 0; i < tramos.size() - 1; i++) {
            Tramo tramoActual = tramos.get(i);
            Tramo siguienteTramo = tramos.get(i + 1);

            // Determinar si el tramoActual termina en un depósito
            int idDestino = tramoActual.getUbicacionOrigen().getIdUbicacion();

            if (idsDepositos.contains(idDestino)) {

                // Obtener depósito
                Deposito deposito = depositoRepository.findById(idDestino)
                        .orElseThrow(() -> new RuntimeException("Depósito no encontrado"));

                // 3. Calcular tiempo entre tramos
                LocalDateTime finActual = tramoActual.getFechaHoraFinReal();
                LocalDateTime inicioSiguiente = siguienteTramo.getFechaHoraInicioReal();

                if (finActual != null && inicioSiguiente != null) {
                    Duration tiempo = Duration.between(finActual, inicioSiguiente);
                    double dias = tiempo.toHours() / 24.0;

                    // 4. Costo acumulado
                    double costo = deposito.getCostoEstadiaDiario() * dias;
                    costoDepositos += costo;
                }
            }
        }

        // 5) QUINTO VALOR – costo por gestión
        double cargosGestion = solicitud.getParametroTarifa().getCargoGestion();
        int cantidadTramos = tramos.size();

        double costoGestion = cargosGestion * cantidadTramos;

        // ================= TOTAL ======================
        double costoTotal =
                costoBaseXVolumenContenedor +
                costoCombustible +
                costoCamionBase +
                costoDepositos +
                costoGestion;
        
        log.info("[SERVICIO] [CALCULAR_COSTO] Desglose -> BaseContenedor: {}, Combustible: {}, BaseCamion: {}, Depositos: {}, Gestion: {}. TOTAL: {}",
                 String.format("%.2f", costoBaseXVolumenContenedor),
                 String.format("%.2f", costoCombustible),
                 String.format("%.2f", costoCamionBase),
                 String.format("%.2f", costoDepositos),
                 String.format("%.2f", costoGestion),
                 String.format("%.2f", costoTotal));

        // --- CÁLCULO TIEMPO REAL  ---
        int tiempoReal = 0;
        if (!tramos.isEmpty()) {
            LocalDateTime inicioViaje = tramos.get(0).getFechaHoraInicioReal();
            LocalDateTime finViaje = tramos.get(tramos.size() - 1).getFechaHoraFinReal();
            if (inicioViaje != null && finViaje != null) {
                tiempoReal = (int) ChronoUnit.MINUTES.between(inicioViaje, finViaje);
            }
        }

        // guardar en la solicitud
        solicitud.setCostoFinal(costoTotal);
        solicitud.setTiempoReal((double)tiempoReal);
        solicitudRepository.save(solicitud); //Chequear 
        
        //retorno el valor
        return costoTotal;
    }
   
    public TramoCamionResponseDTO asignarCamion(int idTramo, String patenteCamion) {
        log.info("[SERVICIO] [ASIGNAR_CAMION] TramoID: {}, Patente: {}", idTramo, patenteCamion);

        // 1) Buscar tramo
        Tramo tramo = tramoRepository.findById(idTramo)
                .orElseThrow(() -> new RuntimeException("El tramo no existe"));

        // 2) Verificar si el camión existe 
        CamionDTO camion = camionClient.obtenerCamionPorPatente(patenteCamion);
        if (camion == null) {
            log.error("[SERVICIO] [ERROR] Camión no encontrado en Flota.");
            throw new RuntimeException("El camión no existe en microservicio Flota");
        }

        // 3) verificar si el camión está disponible y asignarlo
        String disponible = camion.getDisponibilidad();

        // Valor del peso y volumen del contenedor
        Solicitud solicitud = solicitudRepository.findById(tramo.getRuta().getSolicitud().getIdSolicitud())
                .orElseThrow(() -> new RuntimeException("La solicitud no existe"));

        Double volumenContenedor = solicitud.getContenedor().getVolumen();
        Double pesoContenedor = solicitud.getContenedor().getPeso();

        if (disponible.equals("DISPONIBLE") && (camion.getCapacidadPeso() >= pesoContenedor) && (camion.getCapacidadVolumen() >= volumenContenedor)) {
            tramo.setPatenteCamion(camion.getPatente());
            tramo.setEstado(EstadoTramo.ASIGNADO);
            tramoRepository.save(tramo);
            
            log.info("[SERVICIO] [CLIENT_FLOTA] Cambiando estado de Camión {} a NO_DISPONIBLE", patenteCamion);
            camionClient.cambiarDisponibilidadCamion(patenteCamion, "NO_DISPONIBLE");
        } else {
            log.warn("[SERVICIO] [ERROR] Asignación rechazada. Disponible: {}, Capacidad Suficiente: {}", disponible, 
                     (camion.getCapacidadPeso() >= pesoContenedor && camion.getCapacidadVolumen() >= volumenContenedor));
            throw new RuntimeException("El camión no se encuentra disponible o no soporta el contenedor");
        }

        log.info("[SERVICIO] [ASIGNAR_CAMION] Asignación exitosa.");

        // 5) DTO respuesta
        return new TramoCamionResponseDTO(
                tramo.getIdTramo(),
                camion.getPatente(),
                tramo.getEstado().name()
        );
    }

    public TramoTransportistaResponseDTO asignarTransportista(int idTramo, int idTransportista) {
        log.info("[SERVICIO] [ASIGNAR_CHOFER] TramoID: {}, ChoferID: {}", idTramo, idTransportista);

        // 1) Buscar tramo
        Tramo tramo = tramoRepository.findById(idTramo)
                .orElseThrow(() -> new RuntimeException("El tramo no existe"));

        // 2) Verificar transportista
        TransportistaDTO transportista =
                transportistaClient.obtenerTransportistaPorId(idTransportista);

        if (transportista == null) {
            log.error("[SERVICIO] [ERROR] Transportista no encontrado en Flota.");
            throw new RuntimeException("El transportista no se encuentra registrado");
        }

        // 3) Validar superposición de horarios
        validarSuperposicionHoraria(tramo, transportista);

        // 4) Llamar al microservicio de flota → asignar tramo
        log.info("[SERVICIO] [CLIENT_FLOTA] Solicitando asignación en microservicio Flota.");
        transportistaClient.asignarTramoATransportista(idTransportista, idTramo);

        // 5) Asignar transportista al tramo
        tramo.setIdTransportista(idTransportista);
        tramoRepository.save(tramo);

        log.info("[SERVICIO] [ASIGNAR_CHOFER] Asignación exitosa.");

        // 6) Respuesta
        return TramoTransportistaResponseDTO.builder()
                .idTramo(tramo.getIdTramo())
                .estadoTramo(tramo.getEstado().name())
                .idTransportista(idTransportista)
                .build();
    }

    private void validarSuperposicionHoraria(Tramo tramoNuevo, TransportistaDTO transportista) {

        LocalDateTime inicioNuevo = tramoNuevo.getFechaHoraInicioEstimada();
        LocalDateTime finNuevo = tramoNuevo.getFechaHoraFinEstimada();

        if (transportista.getTramosAsignados() == null) {
            return;
        }

        for (Integer idTramoExistente : transportista.getTramosAsignados()) {

            Tramo t = tramoRepository.findById(idTramoExistente)
                    .orElse(null);

            if (t == null) continue;

            LocalDateTime inicio = t.getFechaHoraInicioEstimada();
            LocalDateTime fin = t.getFechaHoraFinEstimada();

            boolean seSuperpone =
                    !inicioNuevo.isAfter(fin) &&
                    !finNuevo.isBefore(inicio);

            if (seSuperpone) {
                log.warn("[SERVICIO] [VALIDACION] Superposición detectada con TramoID: {}", idTramoExistente);
                throw new RuntimeException(
                        "No se puede asignar el tramo debido a una superposición de horarios del transportista");
            }
        }
    }

    public TramosAsignadosResponseDTO obtenerTramosAsignados(List<Integer> idsTramos) {
        log.debug("[SERVICIO] [CONSULTAR_ASIGNADOS] Recuperando info de {} tramos.", idsTramos.size());

        List<TramoAsignadoResponseDTO> lista = new ArrayList<>();

        for (Integer id : idsTramos) {

            Tramo tramo = tramoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tramo no encontrado: " + id));

            UbicacionSimpleDTO origen = UbicacionSimpleDTO.builder()
                    .idUbicacion(tramo.getUbicacionOrigen().getIdUbicacion())
                    .direccion(tramo.getUbicacionOrigen().getDireccion())
                    .build();

            UbicacionSimpleDTO destino = UbicacionSimpleDTO.builder()
                    .idUbicacion(tramo.getUbicacionDestino().getIdUbicacion())
                    .direccion(tramo.getUbicacionDestino().getDireccion())
                    .build();

            TramoAsignadoResponseDTO dto = TramoAsignadoResponseDTO.builder()
                    .idTramo(tramo.getIdTramo())
                    .idRuta(tramo.getRuta().getIdRuta())
                    .origen(origen)
                    .destino(destino)
                    .patenteCamion(tramo.getPatenteCamion())
                    .tipoTramo(tramo.getTipoTramo())
                    .numeroOrden(tramo.getNumeroOrden())
                    .distanciaEstimada(tramo.getDistanciaEstimada())
                    .fechaHoraInicioEstimada(tramo.getFechaHoraInicioEstimada())
                    .fechaHoraFinEstimada(tramo.getFechaHoraFinEstimada())
                    .build();

            lista.add(dto);
        }

        return new TramosAsignadosResponseDTO(lista);
    }
}