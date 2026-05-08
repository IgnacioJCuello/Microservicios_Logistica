package ar.backend.logistica.controller;

import ar.backend.logistica.dto.ContenedorRequestDTO;
import ar.backend.logistica.dto.ContenedorResponseDTO;
import ar.backend.logistica.dto.EstadoContenedorResponseDTO;
import ar.backend.logistica.models.enums.EstadoContenedor;
import ar.backend.logistica.service.ContenedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j; // Importar Lombok

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j // Habilitar logs
@RestController
@RequestMapping("/contenedores")
@Tag(name = "Gestión de Contenedores", description = "Endpoints para el registro, seguimiento y control de estado de los contenedores.")
public class ContenedorController {

    @Autowired
    private ContenedorService contenedorService;

    @Operation(summary = "Registrar ingreso de contenedor", description = "Da de alta un nuevo contenedor en el sistema. Tarea administrativa u operativa.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenedor registrado exitosamente"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos para registrar activos")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<ContenedorResponseDTO> crearContenedor(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del nuevo contenedor", required = true)
            @RequestBody ContenedorRequestDTO request) {
        
        log.info("[LOGISTICA] [CREAR_CONTENEDOR] Solicitud recibida. Procesando alta de nuevo contenedor.");

        ContenedorResponseDTO response = contenedorService.crear(request);
        
        log.info("[LOGISTICA] [CREAR_CONTENEDOR] Contenedor registrado con ID: {}", response.getIdContenedor());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar estado (Tracking)", description = "Permite a clientes y administradores ver dónde está su carga y en qué estado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Contenedor no encontrado")
    })
    @GetMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<EstadoContenedorResponseDTO> obtenerEstadoContendor(
            @Parameter(description = "ID del contenedor a rastrear", example = "1005")
            @PathVariable int id) {
        
        log.info("[LOGISTICA] [TRACKING] Consultando estado para ContenedorID: {}", id);
        
        EstadoContenedorResponseDTO response = contenedorService.obtenerEstado(id);
        
        log.info("[LOGISTICA] [TRACKING] Estado actual: {}", response.getEstado());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar contenedores pendientes", description = "Reporte operativo de contenedores que esperan ser procesados o asignados.")
    @GetMapping("/pendientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<List<ContenedorResponseDTO>> getContenedoresPendientes(
            @Parameter(description = "Filtrar por estado específico (opcional)", example = "EN_DEPOSITO")
            @RequestParam(required = false) EstadoContenedor estado) {
        
        log.info("[LOGISTICA] [PENDIENTES] Consultando contenedores. Filtro estado: {}", (estado != null ? estado : "TODOS"));

        List<ContenedorResponseDTO> lista = contenedorService.consultarContenedoresPendientes(estado);
        
        log.info("[LOGISTICA] [PENDIENTES] Se encontraron {} contenedores.", lista.size());
        return ResponseEntity.ok(lista);
    }
}