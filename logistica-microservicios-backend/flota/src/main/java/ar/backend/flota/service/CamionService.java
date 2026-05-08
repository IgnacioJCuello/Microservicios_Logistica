package ar.backend.flota.service;

import ar.backend.flota.dto.CamionCreateRequestDTO;
import ar.backend.flota.dto.CamionResponseDTO;
import ar.backend.flota.dto.CamionesActualizarDTO;
import ar.backend.flota.dto.CamionesAptosResponseDTO;
import ar.backend.flota.models.Camion;
import ar.backend.flota.repository.CamionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j 
@Service
@RequiredArgsConstructor
public class CamionService {
    private final CamionRepository camionRepository;

    public CamionesAptosResponseDTO obtenerCamionesAptos (double volumen, double peso) {
        log.info("[SERVICIO] [CAMIONES_APTOS] Buscando camiones con CapacidadVolumen >= {} y CapacidadPeso >= {}", volumen, peso);
    
        List<Camion> camiones = camionRepository
                .findByCapacidadVolumenGreaterThanEqualAndCapacidadPesoGreaterThanEqual(volumen, peso);

        log.info("[SERVICIO] [CAMIONES_APTOS] Se encontraron {} vehículos aptos.", camiones.size());

        List<CamionResponseDTO> dtos = camiones.stream()
                .map(c -> new CamionResponseDTO(
                        c.getPatente(),
                        c.getConsumoCombustibleXKilometro(),
                        c.getCapacidadVolumen(),
                        c.getCapacidadPeso(),
                        c.getDisponibilidad()
                ))
                .toList();
        return new CamionesAptosResponseDTO(dtos);
    }

    public CamionResponseDTO crearCamion(CamionCreateRequestDTO request) {
        log.info("[SERVICIO] [CREAR_CAMION] Procesando alta de patente: {}", request.getPatente());
        
        Camion camion = Camion.builder()
                .patente(request.getPatente())
                .capacidadPeso(request.getCapacidadPeso())
                .capacidadVolumen(request.getCapacidadVolumen())
                .consumoCombustibleXKilometro(request.getConsumoCombustibleXKilometro())
                .disponibilidad(request.getDisponibilidad())
                .build();
        
        camionRepository.save(camion);
        
        log.info("[SERVICIO] [CREAR_CAMION] Guardado exitoso en base de datos.");

        return new CamionResponseDTO(
            camion.getPatente(), 
            camion.getConsumoCombustibleXKilometro(),
            camion.getCapacidadVolumen(),
            camion.getCapacidadPeso(),
            camion.getDisponibilidad()
        );
    }

    public CamionResponseDTO obtenerCamionPorPatente(String patente) {
        log.debug("[SERVICIO] [CONSULTA_CAMION] Buscando ID: {}", patente);
        
        Camion camion = camionRepository.findById(patente)
                .orElseThrow(() -> {
                    log.error("[SERVICIO] [ERROR] Camión con patente {} no encontrado", patente);
                    return new RuntimeException("Camión no encontrado");
                });

        return new CamionResponseDTO(
            camion.getPatente(), 
            camion.getConsumoCombustibleXKilometro(),
            camion.getCapacidadVolumen(),
            camion.getCapacidadPeso(),
            camion.getDisponibilidad()
        );
    }

    public void cambiarDisponibilidadCamion(String patente, String disponibilidad) {
        log.info("[SERVICIO] [CAMBIAR_ESTADO] Actualizando patente {} a estado {}", patente, disponibilidad);
        
        Camion camion = camionRepository.findById(patente)
            .orElseThrow(() -> {
                log.error("[SERVICIO] [ERROR] No se puede cambiar estado. Camión {} no existe", patente);
                return new RuntimeException("Camion no encontrado");
            });

        camion.setDisponibilidad(disponibilidad);
        camionRepository.save(camion);
    }

    @Transactional
    public Camion actualizarCamion(String patente, CamionesActualizarDTO dto) {
        log.info("[SERVICIO] [ACTUALIZAR] Iniciando actualización para patente: {}", patente);

        // 1. Buscar el camión existente
        Camion camionExistente = camionRepository.findById(patente)
                .orElseThrow(() -> {
                     log.error("[SERVICIO] [ERROR] Fallo al actualizar. Patente {} no encontrada", patente);
                     return new RuntimeException("No se encontró el camión con patente: " + patente);
                });

        // 2. Actualizar los campos (Solo si vienen en el DTO)
        if (dto.getCapacidadPeso() != null) {
            camionExistente.setCapacidadPeso(dto.getCapacidadPeso());
        }
        if (dto.getCapacidadVolumen() != null) {
            camionExistente.setCapacidadVolumen(dto.getCapacidadVolumen());
        }
        if (dto.getConsumoCombustibleXKilometro() != null) {
            camionExistente.setConsumoCombustibleXKilometro(dto.getConsumoCombustibleXKilometro());
        }
        if (dto.getDisponibilidad() != null) {
            camionExistente.setDisponibilidad(dto.getDisponibilidad());
        }

        // 3. Guardar cambios (Hibernate hace el UPDATE automáticamente al ser transaccional)
        Camion guardado = camionRepository.save(camionExistente);
        
        log.info("[SERVICIO] [ACTUALIZAR] Datos actualizados correctamente.");
        return guardado;
    }
}