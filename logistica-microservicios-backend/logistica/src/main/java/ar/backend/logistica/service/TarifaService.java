package ar.backend.logistica.service;

import ar.backend.logistica.dto.ParametroTarifaRequestDTO;
import ar.backend.logistica.models.ParametroTarifa;
import ar.backend.logistica.repository.ParametroRepository; 
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TarifaService {

    private final ParametroRepository parametroRepository;

    // --- REGISTRAR (Crear nueva tarifa) ---
    @Transactional
    public ParametroTarifa registrarTarifa(ParametroTarifaRequestDTO dto) {
        log.info("[SERVICIO] [CREAR_TARIFA] Procesando alta. Combustible: {}, Gestión: {}", dto.getPrecioCombustible(), dto.getCargoGestion());
        
        ParametroTarifa nuevaTarifa = ParametroTarifa.builder()
                .precioCombustible(dto.getPrecioCombustible())
                .cargoGestion(dto.getCargoGestion())
                .build();
        
        ParametroTarifa guardada = parametroRepository.save(nuevaTarifa);
        
        log.info("[SERVICIO] [CREAR_TARIFA] Guardado exitoso. ID Generado: {}", guardada.getIdParametroTarifa());
        
        return guardada;
    }

    // --- ACTUALIZAR (Modificar tarifa existente) ---
    @Transactional
    public ParametroTarifa actualizarTarifa(int id, ParametroTarifaRequestDTO dto) {
        log.info("[SERVICIO] [ACTUALIZAR_TARIFA] Buscando tarifa ID: {}", id);
        
        // 1. Buscar la tarifa existente
        ParametroTarifa tarifa = parametroRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[SERVICIO] [ERROR] Tarifa ID {} no encontrada.", id);
                    return new RuntimeException("Tarifa no encontrada ID: " + id);
                });

        // 2. Actualizar valores
        tarifa.setPrecioCombustible(dto.getPrecioCombustible());
        tarifa.setCargoGestion(dto.getCargoGestion());

        // 3. Guardar
        ParametroTarifa actualizada = parametroRepository.save(tarifa);
        
        log.info("[SERVICIO] [ACTUALIZAR_TARIFA] Tarifa actualizada correctamente.");
        
        return actualizada;
    }

    public List<ParametroTarifa> obtenerTodas() {
        log.info("[SERVICIO] [LISTAR_TARIFAS] Consultando todas las tarifas configuradas.");
        
        List<ParametroTarifa> lista = parametroRepository.findAll();
        
        log.info("[SERVICIO] [LISTAR_TARIFAS] Consulta finalizada. Total registros: {}", lista.size());
        
        return lista;
    }
}