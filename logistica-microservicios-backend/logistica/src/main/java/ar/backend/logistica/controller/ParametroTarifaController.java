package ar.backend.logistica.controller;

import ar.backend.logistica.models.ParametroTarifa;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ar.backend.logistica.dto.ParametroTarifaRequestDTO;
import ar.backend.logistica.service.TarifaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Importar Lombok

import java.util.List;

@Slf4j // Habilitar logs
@RestController
@RequestMapping("/tarifas")
@RequiredArgsConstructor
@Tag(name = "Gestión de Tarifas", description = "Endpoints para la configuración de costos base, precios de combustible y cargos administrativos.")
public class ParametroTarifaController {

    private final TarifaService tarifaService;

    // ALTA: Definir precios es una decisión gerencial.
    @Operation(summary = "Registrar nuevas tarifas", description = "Define un nuevo conjunto de parámetros de costos para el sistema. Requiere rol ADMIN u OPERADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tarifas registradas exitosamente"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos para definir precios")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<ParametroTarifa> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Valores de los costos a registrar", required = true)
            @RequestBody ParametroTarifaRequestDTO dto) {
        
        log.info("[LOGISTICA] [TARIFAS] Registrando nueva configuración. PrecioCombustible: {}, CargoGestion: {}", 
                 dto.getPrecioCombustible(), dto.getCargoGestion());

        ParametroTarifa creada = tarifaService.registrarTarifa(dto);
        
        log.info("[LOGISTICA] [TARIFAS] Configuración guardada exitosamente. ID: {}", creada.getIdParametroTarifa());
        return ResponseEntity.status(201).body(creada);
    }

    // MODIFICACIÓN: Actualizar precios es una decisión gerencial.
    @Operation(summary = "Actualizar tarifas existentes", description = "Modifica los valores de combustible o gestión de una tarifa específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarifa actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró la tarifa con el ID especificado"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos para modificar precios")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<ParametroTarifa> actualizar(
            @Parameter(description = "ID de la tarifa a modificar", example = "1")
            @PathVariable int id,
            
            @RequestBody ParametroTarifaRequestDTO dto) {
        
        log.info("[LOGISTICA] [TARIFAS] Solicitud de actualización para TarifaID: {}", id);
        
        ParametroTarifa actualizada = tarifaService.actualizarTarifa(id, dto);
        
        log.info("[LOGISTICA] [TARIFAS] Valores actualizados correctamente.");
        return ResponseEntity.ok(actualizada);
    }

    // CONSULTA: El Operador necesita ver las tarifas para su trabajo diario.
    @Operation(summary = "Listar todas las tarifas", description = "Recupera el historial completo de configuraciones de precios.")
    @ApiResponse(responseCode = "200", description = "Lista de tarifas obtenida exitosamente")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<List<ParametroTarifa>> listar() {
        log.info("[LOGISTICA] [TARIFAS] Consultando historial de configuraciones.");
        
        List<ParametroTarifa> lista = tarifaService.obtenerTodas();
        
        log.info("[LOGISTICA] [TARIFAS] Consulta finalizada. Registros encontrados: {}", lista.size());
        return ResponseEntity.ok(lista);
    }
}