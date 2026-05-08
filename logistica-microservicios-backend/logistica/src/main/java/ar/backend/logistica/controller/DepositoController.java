package ar.backend.logistica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.backend.logistica.dto.DepositoRequestDTO;
import ar.backend.logistica.dto.DepositoResponseDTO;
import ar.backend.logistica.dto.ContenedoresDepositoDTO;
import ar.backend.logistica.service.DepositoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/depositos")
@RequiredArgsConstructor
@Tag(name = "Gestión de Depósitos", description = "Endpoints para la administración de puntos físicos de almacenamiento intermedio.")
public class DepositoController {

    private final DepositoService depositoService;

    // ALTA: Solo Alta Gerencia define dónde hay depósitos físicos
    @Operation(summary = "Crear nuevo depósito", description = "Registra un nuevo punto de almacenamiento en el sistema. Requiere rol de ADMIN u OPERADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Depósito creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos para crear depósitos")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<DepositoResponseDTO> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Información del depósito a crear", required = true)
            @RequestBody DepositoRequestDTO dto) {
        
        log.info("[LOGISTICA] [CREAR_DEPOSITO] Recibida solicitud de alta.");
        
        DepositoResponseDTO response = depositoService.crear(dto);
        
        log.info("[LOGISTICA] [CREAR_DEPOSITO] Depósito creado exitosamente con ID: {}", response.getIdDeposito());
        return ResponseEntity.ok(response);
    }

    // MODIFICACIÓN: Solo Alta Gerencia actualiza datos del depósito
    @Operation(summary = "Actualizar depósito existente", description = "Modifica los datos comerciales o de ubicación de un depósito. Requiere rol de ADMIN u OPERADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Depósito actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró el depósito con el ID especificado"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos para modificar depósitos")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<DepositoResponseDTO> actualizar(
            @Parameter(description = "ID único del depósito a modificar", example = "1") 
            @PathVariable int id, 
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del depósito", required = true)
            @RequestBody DepositoRequestDTO dto) {
        
        log.info("[LOGISTICA] [ACTUALIZAR_DEPOSITO] Modificando DepositoID: {}", id);
        
        DepositoResponseDTO response = depositoService.actualizar(id, dto);
        
        log.info("[LOGISTICA] [ACTUALIZAR_DEPOSITO] Actualización exitosa.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/contenedores")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<ContenedoresDepositoDTO> consultarContenedores(
        @Parameter(description = "Identificador único del depósito a consultar", example = "5")
        @PathVariable int id) {
            
            log.info("[LOGISTICA] [STOCK_DEPOSITO] Consultando stock para DepositoID: {}", id);
            
            ContenedoresDepositoDTO response = depositoService.consultarContenedores(id);
            
            log.info("[LOGISTICA] [STOCK_DEPOSITO] Consulta finalizada. Total contenedores: {}", 
                     (response.getContenedores() != null ? response.getContenedores().size() : 0));
            
            return ResponseEntity.ok(response);
        }
}