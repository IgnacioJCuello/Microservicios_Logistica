package ar.backend.logistica.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Service;
import ar.backend.logistica.client.*;
import ar.backend.logistica.dto.*;
import ar.backend.logistica.models.*;
import ar.backend.logistica.models.enums.*;
import ar.backend.logistica.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitudService {
    
    private final ClienteRepository clienteRepository;
    private final UbicacionRepository ubicacionRepository;
    private final ParametroRepository parametrosTarifaRepository;
    private final ContenedorRepository contenedorRepository;
    private final SolicitudRepository solicitudRepository;
    private final RutaRepository rutaRepository;
    private final TramoRepository tramoRepository;
    private final OsrmClient osrmClient;
    private final CamionClient camionClient;

    @Transactional
    public SolicitudResponseDTO crearSolicitud(SolicitudCreateRequest request) {
        log.info("[SERVICIO] [CREAR_SOLICITUD] Procesando nueva solicitud. Cliente Email: {}", request.getCliente().getEmail());

        // 1. CLIENTE – existe o crear uno nuevo
        Cliente cliente = clienteRepository.findByEmail(request.getCliente().getEmail())
                .orElseGet(() -> {
                    log.info("[SERVICIO] [CREAR_SOLICITUD] Cliente nuevo detectado. Registrando: {} {}", 
                             request.getCliente().getNombre(), request.getCliente().getApellido());
                    Cliente nuevo = new Cliente();
                    nuevo.setNombre(request.getCliente().getNombre());
                    nuevo.setApellido(request.getCliente().getApellido());
                    nuevo.setTelefono(request.getCliente().getTelefono());
                    nuevo.setEmail(request.getCliente().getEmail());
                    return clienteRepository.save(nuevo);
                });

        // 2. UBICACIÓN ORIGEN — buscar o crear
        Ubicacion origen = ubicacionRepository
                .findByLatitudAndLongitud(request.getUbicacionOrigen().getLatitud(), request.getUbicacionOrigen().getLongitud())
                .orElseGet(() -> {
                    Ubicacion nueva = new Ubicacion();
                    nueva.setDireccion(request.getUbicacionOrigen().getDireccion());
                    nueva.setLatitud(request.getUbicacionOrigen().getLatitud());
                    nueva.setLongitud(request.getUbicacionOrigen().getLongitud());
                    return ubicacionRepository.save(nueva);
                });

        // 3. UBICACIÓN DESTINO — buscar o crear
        Ubicacion destino = ubicacionRepository
                .findByLatitudAndLongitud(request.getUbicacionDestino().getLatitud(), request.getUbicacionDestino().getLongitud())
                .orElseGet(() -> {
                    Ubicacion nueva = new Ubicacion();
                    nueva.setDireccion(request.getUbicacionDestino().getDireccion());
                    nueva.setLatitud(request.getUbicacionDestino().getLatitud());
                    nueva.setLongitud(request.getUbicacionDestino().getLongitud());
                    return ubicacionRepository.save(nueva);
                });

        // 4. PARÁMETROS DE TARIFA — buscar o crear
        ParametroTarifa tarifa = parametrosTarifaRepository
                .findByPrecioCombustibleAndCargoGestion(request.getParametroTarifa().getPrecioCombustible(), request.getParametroTarifa().getCargoGestion())
                .orElseGet(() -> {
                    ParametroTarifa nuevo = new ParametroTarifa();
                    nuevo.setPrecioCombustible(request.getParametroTarifa().getPrecioCombustible());
                    nuevo.setCargoGestion(request.getParametroTarifa().getCargoGestion());
                    return parametrosTarifaRepository.save(nuevo);
                });

        // 5. CONTENEDOR – viene ya creado      
        Contenedor contenedor = contenedorRepository.findById(request.getIdContenedor())
                .orElseThrow(() -> {
                    log.error("[SERVICIO] [CREAR_SOLICITUD] Error: Contenedor ID {} no encontrado", request.getIdContenedor());
                    return new RuntimeException("El contenedor no existe");
                });

        // 6. CREAR LA SOLICITUD
        Solicitud solicitud = new Solicitud();
        solicitud.setCliente(cliente);
        solicitud.setUbicacionOrigen(origen);
        solicitud.setUbicacionDestino(destino);
        solicitud.setParametroTarifa(tarifa);
        solicitud.setContenedor(contenedor);
        contenedor.setEstado(EstadoContenedor.EN_ORIGEN);
        solicitud.setEstado(EstadoSolicitud.BORRADOR);
        solicitud.setFechaHoraCreacion(java.time.LocalDateTime.now());

        // 7. GUARDAR LA SOLICITUD
        Solicitud guardada = solicitudRepository.save(solicitud);

        log.info("[SERVICIO] [CREAR_SOLICITUD] Solicitud guardada exitosamente. ID: {}", guardada.getIdSolicitud());

        // 8. PREPARAR Y DEVOLVER LA RESPUESTA
        ClienteResponseDTO clienteDTO = new ClienteResponseDTO(
            guardada.getCliente().getIdCliente(),
            guardada.getCliente().getNombre(),
            guardada.getCliente().getApellido(),
            guardada.getCliente().getEmail(),
            guardada.getCliente().getTelefono()
        );
        UbicacionResponseDTO origenDTO = new UbicacionResponseDTO(
            guardada.getUbicacionOrigen().getIdUbicacion(),
            guardada.getUbicacionOrigen().getDireccion(),
            guardada.getUbicacionOrigen().getLatitud(),
            guardada.getUbicacionOrigen().getLongitud()
        );
        UbicacionResponseDTO destinoDTO = new UbicacionResponseDTO(
            guardada.getUbicacionDestino().getIdUbicacion(),
            guardada.getUbicacionDestino().getDireccion(),
            guardada.getUbicacionDestino().getLatitud(),
            guardada.getUbicacionDestino().getLongitud()
        );
        ParametroTarifaResponseDTO tarifaDTO = new ParametroTarifaResponseDTO(
            guardada.getParametroTarifa().getIdParametroTarifa(),
            guardada.getParametroTarifa().getPrecioCombustible(),
            guardada.getParametroTarifa().getCargoGestion()
        );

        return new SolicitudResponseDTO(
            guardada.getIdSolicitud(),
            guardada.getEstado().name(),
            guardada.getFechaHoraCreacion(),
            clienteDTO,
            origenDTO,
            destinoDTO,
            tarifaDTO,
            guardada.getContenedor().getIdContenedor()
        );
    }

    @Transactional
    public RutaTentativaResponse crearRutaTentativa(int idSolicitud, RutaTentativaRequestDTO request) {
        log.info("[SERVICIO] [RUTA_TENTATIVA] Calculando ruta para SolicitudID: {}", idSolicitud);

        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
            .orElseThrow(() -> new RuntimeException("La solicitud no existe"));
        
        // validar que los tramos estén ordenados por numeroOrden
        List<TramoRequestDTO> tramos = request.getTramos();
        tramos.sort(Comparator.comparingInt(TramoRequestDTO::getNumeroOrden));

        // el primer tramo trae la fechaHoraInicioEstimada
        LocalDateTime inicioPrimerTramo = tramos.get(0).getFechaHoraInicioEstimada();

        Ruta ruta = new Ruta();
        ruta.setSolicitud(solicitud);
        ruta = rutaRepository.save(ruta);

        // inicializamos los acumuladores en cero  
        double tiempoTotalEstimado = 0.0; // en minutos
        LocalDateTime nextStart = inicioPrimerTramo;
        double distanciaRutaTotal = 0.0;

        for (TramoRequestDTO tramoDTO : tramos) {
            
            // 1) buscar o crear las ubicaciones
            Ubicacion origen = ubicacionRepository
                .findByLatitudAndLongitud(tramoDTO.getUbicacionOrigen().getLatitud(), tramoDTO.getUbicacionOrigen().getLongitud())
                .orElseGet(() -> {
                    Ubicacion nueva = new Ubicacion();
                    nueva.setDireccion(tramoDTO.getUbicacionOrigen().getDireccion());
                    nueva.setLatitud(tramoDTO.getUbicacionOrigen().getLatitud());
                    nueva.setLongitud(tramoDTO.getUbicacionOrigen().getLongitud());
                    return ubicacionRepository.save(nueva);
                });
                    
            Ubicacion destino = ubicacionRepository
                .findByLatitudAndLongitud(tramoDTO.getUbicacionDestino().getLatitud(), tramoDTO.getUbicacionDestino().getLongitud())
                .orElseGet(() -> {
                    Ubicacion nueva = new Ubicacion();
                    nueva.setDireccion(tramoDTO.getUbicacionDestino().getDireccion());
                    nueva.setLatitud(tramoDTO.getUbicacionDestino().getLatitud());
                    nueva.setLongitud(tramoDTO.getUbicacionDestino().getLongitud());
                    return ubicacionRepository.save(nueva);
                });

            // 2) llamar a OSRM para obtener la distancia y la duración
            log.debug("[SERVICIO] [OSRM] Consultando tramo: {} -> {}", origen.getDireccion(), destino.getDireccion());
            OsrmResponse os = osrmClient.route(origen.getLatitud(), origen.getLongitud(),
                                            destino.getLatitud(), destino.getLongitud());
            double distanciaKm = os.getDistanceMeters() / 1000.0;
            double duracion = os.getDurationSeconds() / 60.0; // convertir a minutos

            // 3) calcular fechas estimadas
            LocalDateTime fechaInicioEstimada = (tramoDTO.getNumeroOrden() == 1) ? nextStart : nextStart;
            LocalDateTime fechaFinEstimada = fechaInicioEstimada.plusMinutes((long)duracion);

            // 4) crear y guardar el Tramo
            Tramo tramo = new Tramo();
            tramo.setRuta(ruta);
            tramo.setNumeroOrden(tramoDTO.getNumeroOrden());
            tramo.setTipoTramo(tramoDTO.getTipoTramo());
            tramo.setUbicacionOrigen(origen);
            tramo.setUbicacionDestino(destino);
            tramo.setDistanciaEstimada(distanciaKm);
            tramo.setFechaHoraInicioEstimada(fechaInicioEstimada);
            tramo.setFechaHoraFinEstimada(fechaFinEstimada);
            tramo.setEstado(EstadoTramo.PROGRAMADO);

            tramoRepository.save(tramo);

            // 5) acumular valores estimados de duración y distancia
            tiempoTotalEstimado += duracion;
            distanciaRutaTotal += distanciaKm;

            // 6) preparar nextStart para el próximo tramo suponiendo que no hay tiempos de espera en el despósito
            nextStart = fechaFinEstimada;
        }

        solicitud.setDistanciaTotal(distanciaRutaTotal);
        
        log.info("[SERVICIO] [RUTA_TENTATIVA] Tramos procesados. Distancia Total: {} km. Tiempo Total: {} min.", 
                 String.format("%.2f", distanciaRutaTotal), String.format("%.2f", tiempoTotalEstimado));

        // ====================== CALCULO DE COSTO ESTIMADO ==========================

        // DISTANCIA TOTAL DE LA RUTA YA LA TENGO
        double distanciaTotalKm = distanciaRutaTotal;

        // 1) PRIMER VALOR – Costo base según volumen del contenedor
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

        double costoBaseXVolumenContenedor = costoBaseKm * distanciaTotalKm;


        // 2) SEGUNDO VALOR – costo de combustible

        ParametroTarifa tarifa = solicitud.getParametroTarifa();
        double precioLitroCombustible = tarifa.getPrecioCombustible();

        // buscamos camiones aptos según el peso y volumen del contenedor
        log.info("[SERVICIO] [CAMION_CLIENT] Buscando camiones aptos. Volumen: {}, Peso: {}", 
                 volumenContenedor, solicitud.getContenedor().getPeso());

        List<CamionDTO> camionesAptos = camionClient.obtenerCamionesAptos(
                solicitud.getContenedor().getVolumen(),
                solicitud.getContenedor().getPeso()
        );

        // calculamos el consumo promedio de combustible por km de los camiones aptos
        double consumoCombustiblePromedioXKilometro = camionesAptos.stream()
                .mapToDouble(CamionDTO::getConsumoCombustibleXKilometro)
                .average()
                .orElseThrow(() -> {
                    log.error("[SERVICIO] [CAMION_CLIENT] No se encontraron camiones aptos.");
                    return new RuntimeException("No hay camiones aptos disponibles");
                });

        double litrosConsumidos = consumoCombustiblePromedioXKilometro * distanciaTotalKm;
        double costoCombustible =  litrosConsumidos * precioLitroCombustible;

        // 3) TERCER VALOR – costo promedio base de camiones según capacidad de volumen

        double costoBasePromedioXVolumenCamiones = camionesAptos.stream()
                .mapToDouble(CamionDTO::getCapacidadVolumen)
                .average()
                .orElse(0.0);
                
        // estructura simple: capacidad de volumen del camión → costo base por km
        Map<Double, Double> costoBasePorCapacidadVolumen = Map.of(
                10.0, 50.0,   // camión chico  → $50/km
                20.0, 80.0,   // camión mediano→ $80/km
                25.0, 100.0,  // camión mediano-grande → $100/km
                30.0, 120.0   // camión grande → $120/km
        );

        // obtener el costo base según el volumen del camión
        double costoBasePorKm = costoBasePorCapacidadVolumen.entrySet().stream()
                .filter(entry -> costoBasePromedioXVolumenCamiones <= entry.getKey())
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(150.0); // valor por defecto si el contenedor supera todos los límites

        // calcular costo total de camiones
        double costoBaseXVolumenCamiones = costoBasePorKm * distanciaTotalKm;

        // 4) CUARTO VALOR – costo por gestión

        double cargosGestion = tarifa.getCargoGestion();
        int cantidadTramos = tramos.size();

        double costoGestion = cargosGestion * cantidadTramos;

        // ================= TOTAL ======================
        double costoTotalEstimado =
                costoBaseXVolumenContenedor +
                costoCombustible +
                costoBaseXVolumenCamiones +
                costoGestion;

        log.info("[SERVICIO] [COSTOS] Calculo finalizado. BaseContenedor: {}, Combustible: {}, BaseCamiones: {}, Gestion: {}. TOTAL: {}",
                 String.format("%.2f", costoBaseXVolumenContenedor),
                 String.format("%.2f", costoCombustible),
                 String.format("%.2f", costoBaseXVolumenCamiones),
                 String.format("%.2f", costoGestion),
                 String.format("%.2f", costoTotalEstimado));

        // guardar en la solicitud
        ruta.setCostoEstimado(costoTotalEstimado);
        ruta.setTiempoEstimado(tiempoTotalEstimado); // ya está en minutos
        rutaRepository.save(ruta);

        // ====================== PREPARAR RESPUESTA ==========================
        
        // obtener todos los tramos guardados
        List<Tramo> tramosGuardados = tramoRepository.findByRutaIdRuta(ruta.getIdRuta());

        // convertir tramos a DTO
        List<TramoSugeridoDTO> tramosSugeridosDTO = tramosGuardados.stream()
                .map(tramo -> new TramoSugeridoDTO(
                        tramo.getIdTramo(),
                        tramo.getNumeroOrden(),
                        new UbicacionResponseDTO(
                                tramo.getUbicacionOrigen().getIdUbicacion(),
                                tramo.getUbicacionOrigen().getDireccion(),
                                tramo.getUbicacionOrigen().getLatitud(),
                                tramo.getUbicacionOrigen().getLongitud()
                        ),
                        new UbicacionResponseDTO(
                                tramo.getUbicacionDestino().getIdUbicacion(),
                                tramo.getUbicacionDestino().getDireccion(),
                                tramo.getUbicacionDestino().getLatitud(),
                                tramo.getUbicacionDestino().getLongitud()
                        ),
                        tramo.getTipoTramo(),
                        tramo.getDistanciaEstimada(),
                        Duration.between(
                                tramo.getFechaHoraInicioEstimada(),
                                tramo.getFechaHoraFinEstimada()
                        ),
                        tramo.getFechaHoraInicioEstimada(),
                        tramo.getFechaHoraFinEstimada(),
                        tramo.getEstado().name()
                ))
                .toList();

        // crear y devolver la respuesta
        return new RutaTentativaResponse(
                ruta.getIdRuta(),
                ruta.getCostoEstimado(),
                ruta.getTiempoEstimado(),
                tramosSugeridosDTO
        );
    }

    public RutasTentativasResponse listarRutasTentativas(int idSolicitud) {
        log.info("[SERVICIO] [LISTAR_RUTAS] Consultando rutas para SolicitudID: {}", idSolicitud);

        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("La solicitud no existe"));

        List<Ruta> rutasTentativas = solicitud.getRutasTentativas();

        // Convertimos cada ruta a DTO
        List<RutaTentativaResponse> rutasDTO = rutasTentativas.stream().map(ruta -> {

            List<TramoSugeridoDTO> tramosDTO = ruta.getTramos().stream().map(tramo ->
                    new TramoSugeridoDTO(
                            tramo.getIdTramo(),
                            tramo.getNumeroOrden(),
                            new UbicacionResponseDTO(
                                    tramo.getUbicacionOrigen().getIdUbicacion(),
                                    tramo.getUbicacionOrigen().getDireccion(),
                                    tramo.getUbicacionOrigen().getLatitud(),
                                    tramo.getUbicacionOrigen().getLongitud()
                            ),
                            new UbicacionResponseDTO(
                                    tramo.getUbicacionDestino().getIdUbicacion(),
                                    tramo.getUbicacionDestino().getDireccion(),
                                    tramo.getUbicacionDestino().getLatitud(),
                                    tramo.getUbicacionDestino().getLongitud()
                            ),
                            tramo.getTipoTramo(),
                            tramo.getDistanciaEstimada(),
                            Duration.between(
                                    tramo.getFechaHoraInicioEstimada(),
                                    tramo.getFechaHoraFinEstimada()
                            ),
                            tramo.getFechaHoraInicioEstimada(),
                            tramo.getFechaHoraFinEstimada(),
                            tramo.getEstado().name()
                    )
            ).toList();

            return new RutaTentativaResponse(
                    ruta.getIdRuta(),
                    ruta.getCostoEstimado(),
                    ruta.getTiempoEstimado(),
                    tramosDTO
            );
        }).toList();  

        return new RutasTentativasResponse(
                solicitud.getIdSolicitud(),
                rutasDTO
        );
    }

    // ---REQUERIMIENTO 4: Asignar una ruta definitiva a la solicitud ---
    public SolicitudConRutaDTO asignarRutaASolicitud(AsignacionRutaDTO asignacion) {
        log.info("[SERVICIO] [ASIGNAR_RUTA] Procesando asignación. SolicitudID: {}, RutaID: {}", 
                 asignacion.getIdSolicitud(), asignacion.getIdRutaTentativa());

        Solicitud solicitud = solicitudRepository.findById(asignacion.getIdSolicitud())
                .orElseThrow(() -> new RuntimeException("La solicitud no existe"));

        Ruta ruta = rutaRepository.findById(asignacion.getIdRutaTentativa())
                .orElseThrow(() -> new RuntimeException("La ruta tentativa no existe"));

        // VALIDACIÓN: La ruta tiene que pertenecer a la misma solicitud
        if (ruta.getSolicitud().getIdSolicitud() != (solicitud.getIdSolicitud())) {
                log.error("[SERVICIO] [ASIGNAR_RUTA] Error: RutaID {} no pertenece a SolicitudID {}", 
                          ruta.getIdRuta(), solicitud.getIdSolicitud());
                throw new RuntimeException("La ruta no pertenece a la solicitud indicada");
        }

        // Asignar la ruta como definitiva
        solicitud.setRutaAsignada(ruta);
        solicitud.setEstado(EstadoSolicitud.PROGRAMADA);
        solicitud.setCostoEstimado(ruta.getCostoEstimado());
        solicitud.setTiempoEstimado(ruta.getTiempoEstimado());

        solicitudRepository.save(solicitud);

        log.info("[SERVICIO] [ASIGNAR_RUTA] Ruta asignada con éxito. Estado actualizado a PROGRAMADA.");

        return new SolicitudConRutaDTO(
                solicitud.getIdSolicitud(),
                solicitud.getCliente().getIdCliente(),
                solicitud.getContenedor().getIdContenedor(),
                solicitud.getUbicacionOrigen().getIdUbicacion(),
                solicitud.getUbicacionDestino().getIdUbicacion(),
                solicitud.getParametroTarifa().getIdParametroTarifa(),
                solicitud.getFechaHoraCreacion(),
                solicitud.getEstado().name(),
                solicitud.getCostoEstimado(),
                solicitud.getTiempoEstimado(),
                solicitud.getRutaAsignada().getIdRuta()
        );
    }
}