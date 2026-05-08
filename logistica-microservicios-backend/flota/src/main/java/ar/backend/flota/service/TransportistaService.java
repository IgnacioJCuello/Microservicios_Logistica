package ar.backend.flota.service;

import ar.backend.flota.client.TramoClient;
import ar.backend.flota.dto.AsignarTramoTransportistaRequestDTO;
import ar.backend.flota.dto.CreateTransportistaRequestDTO;
import ar.backend.flota.dto.CreateTransportistaResponseDTO;
import ar.backend.flota.dto.TramosAsignadosResponseDTO;
import ar.backend.flota.dto.TransportistaDTO;
import ar.backend.flota.models.Transportista;
import ar.backend.flota.repository.TransportistaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransportistaService {

    private final TransportistaRepository transportistaRepository;
    private final TramoClient tramoClient;

    public Transportista getTransportistaById(int id) {
        return transportistaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[SERVICIO] [ERROR] Chofer ID {} no encontrado.", id);
                    return new RuntimeException("Transportista no encontrado con ID: " + id);
                });
    }

    public List<Transportista> getAllTransportistas() {
        return transportistaRepository.findAll();
    }

    public CreateTransportistaResponseDTO createTransportista(CreateTransportistaRequestDTO transportistaDTO) {
        log.info("[SERVICIO] [CREAR_CHOFER] Procesando registro. Telefono: {}", transportistaDTO.getTelefono());
        
        Transportista transportista = transportistaRepository.findByTelefono(transportistaDTO.getTelefono())
                .orElseGet(() -> {
                    log.info("[SERVICIO] [CREAR_CHOFER] Usuario nuevo. Guardando en BD...");
                    Transportista nuevo = new Transportista();
                    nuevo.setNombre(transportistaDTO.getNombre());
                    nuevo.setApellido(transportistaDTO.getApellido());
                    nuevo.setTelefono(transportistaDTO.getTelefono());
                    return transportistaRepository.save(nuevo);
                });
        
        log.info("[SERVICIO] [CREAR_CHOFER] Chofer procesado correctamente. ID: {}", transportista.getIdTransportista());
                
        return CreateTransportistaResponseDTO.builder()
            .idTransportista(transportista.getIdTransportista()) 
            .nombre(transportista.getNombre())
            .apellido(transportista.getApellido())
            .telefono(transportista.getTelefono())
            .build();
    }

    public TransportistaDTO obtenerTransportistaPorId(int idTransportista) {
        Transportista t = transportistaRepository.findById(idTransportista)
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado"));

        return TransportistaDTO.builder()
                .idTransportista(t.getIdTransportista())
                .nombre(t.getNombre())
                .tramosAsignados(t.getTramosAsignados())
                .build();
    }

    public void asignarTramo(AsignarTramoTransportistaRequestDTO dto) {
        log.info("[SERVICIO] [ASIGNAR] Buscando ChoferID: {}", dto.getIdTransportista());

        Transportista transportista =
                transportistaRepository.findById(dto.getIdTransportista())
                        .orElseThrow(() -> new RuntimeException("Transportista no encontrado"));

        if (transportista.getTramosAsignados() == null)
            transportista.setTramosAsignados(new ArrayList<>());

        // Evitar duplicados
        if (!transportista.getTramosAsignados().contains(dto.getIdTramo())) {
            transportista.getTramosAsignados().add(dto.getIdTramo());
            log.info("[SERVICIO] [ASIGNAR] TramoID {} agregado a la lista del chofer.", dto.getIdTramo());
        } else {
            log.warn("[SERVICIO] [ASIGNAR] El TramoID {} ya estaba asignado a este chofer.", dto.getIdTramo());
        }

        transportistaRepository.save(transportista);
    }

    public TramosAsignadosResponseDTO obtenerTramosAsignados(int idTransportista) {
        log.info("[SERVICIO] [CONSULTA_TRAMOS] Verificando asignaciones para ChoferID: {}", idTransportista);

        // validar que existe el transportista
        Transportista transportista = transportistaRepository.findById(idTransportista)
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado"));

        // obtener lista de IDs de tramos
        List<Integer> idsTramos = transportista.getTramosAsignados();
        
        if (idsTramos == null || idsTramos.isEmpty()) {
            log.info("[SERVICIO] [CONSULTA_TRAMOS] El chofer no tiene viajes asignados.");
            return new TramosAsignadosResponseDTO(List.of());
        }

        log.info("[SERVICIO] [CLIENT_LOGISTICA] Solicitando detalles de {} tramos al microservicio Logistica...", idsTramos.size());

        // llamar al microservicio logística
        return tramoClient.obtenerTramosPorIds(idsTramos);
    }

    public void desasignarTramo(int idTransportista, int idTramo) {
        log.info("[SERVICIO] [DESASIGNAR] Removiendo TramoID: {} de ChoferID: {}", idTramo, idTransportista);
        
        Transportista transportista = transportistaRepository.findById(idTransportista)
                .orElseThrow(() -> new RuntimeException("El transportista no se encuentra registrado"));
        
        if (transportista.getTramosAsignados() != null) {
            boolean removed = transportista.getTramosAsignados().remove(Integer.valueOf(idTramo));
            if (removed) {
                transportistaRepository.save(transportista);
                log.info("[SERVICIO] [DESASIGNAR] Eliminación exitosa.");
            } else {
                log.warn("[SERVICIO] [DESASIGNAR] El tramo no estaba en la lista del chofer.");
            }
        }    
    }
}