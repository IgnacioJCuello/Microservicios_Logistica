package ar.backend.logistica.service;

import org.springframework.stereotype.Service;

import ar.backend.logistica.dto.ContenedorRequestDTO;
import ar.backend.logistica.dto.ContenedorResponseDTO;
import ar.backend.logistica.dto.EstadoContenedorResponseDTO;
import ar.backend.logistica.models.Contenedor;
import ar.backend.logistica.models.enums.EstadoContenedor;
import ar.backend.logistica.repository.ContenedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j 
@Service
@RequiredArgsConstructor
public class ContenedorService {
    private final ContenedorRepository contenedorRepository;

    public ContenedorResponseDTO crear(ContenedorRequestDTO request) {
        log.info("[SERVICIO] [CREAR_CONTENEDOR] Procesando alta. Peso: {}, Volumen: {}", request.getPeso(), request.getVolumen());

        Contenedor contenedor = new Contenedor();
        contenedor.setPeso(request.getPeso());
        contenedor.setVolumen(request.getVolumen());
        contenedor.setEstado(EstadoContenedor.CREADO);
        
        Contenedor guardado = contenedorRepository.save(contenedor);
        
        log.info("[SERVICIO] [CREAR_CONTENEDOR] Guardado exitoso. ID Generado: {}", guardado.getIdContenedor());

        return new ContenedorResponseDTO(
                guardado.getIdContenedor(),
                guardado.getPeso(),
                guardado.getVolumen(),
                guardado.getEstado().name()
        );
    }

    public EstadoContenedorResponseDTO obtenerEstado(int id) {
        // Log nivel debug para no saturar si hay mucho tracking
        log.debug("[SERVICIO] [TRACKING] Buscando contenedor ID: {}", id); 

        Contenedor contenedor = contenedorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[SERVICIO] [ERROR] Contenedor ID {} no encontrado.", id);
                    return new RuntimeException("Contenedor no encontrado");
                });
        
        return new EstadoContenedorResponseDTO(contenedor.getIdContenedor(), contenedor.getEstado().name());
    }

    // --- REQUERIMIENTO 5: Consultar contenedores pendientes con filtros ---
    public List<ContenedorResponseDTO> consultarContenedoresPendientes (EstadoContenedor estadoFiltro){
        
        log.info("[SERVICIO] [PENDIENTES] Consultando listado. Filtro aplicado: {}", (estadoFiltro != null ? estadoFiltro : "NINGUNO (Todos los no entregados)"));

        List<Contenedor> contenedores;

        if(estadoFiltro != null){
            contenedores = contenedorRepository.findByEstado(estadoFiltro);
        } else{
            contenedores = contenedorRepository.findByEstadoNot(EstadoContenedor.ENTREGADO);
        }

        log.info("[SERVICIO] [PENDIENTES] Resultados encontrados: {}", contenedores.size());

        return contenedores.stream()
                .map(c -> new ContenedorResponseDTO(
                    c.getIdContenedor(),
                    c.getPeso(),
                    c.getVolumen(),
                    c.getEstado().name()
                ))
                .collect(Collectors.toList());
    }
}