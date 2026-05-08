package ar.backend.flota.controller;

import ar.backend.flota.dto.CamionCreateRequestDTO;
import ar.backend.flota.dto.CamionResponseDTO;
import ar.backend.flota.dto.CamionesAptosResponseDTO;
import ar.backend.flota.dto.CamionesActualizarDTO;
import ar.backend.flota.models.Camion;
import ar.backend.flota.service.CamionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; 

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j 
@RestController
@RequestMapping("/camiones")
@RequiredArgsConstructor
@Tag(name = "Gestión de Camiones", description = "Endpoints para administrar la flota, consultar disponibilidad y características técnicas de los vehículos.")
public class CamionController {

    private final CamionService camionService;

    @Operation(summary = "Buscar camiones aptos", description = "Devuelve una lista de camiones que soportan el peso y volumen solicitados. Uso operativo para asignación de rutas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda exitosa"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar la búsqueda")
    })
    @GetMapping("/aptos")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<CamionesAptosResponseDTO> obtenerCamionesAptos(
            @Parameter(description = "Volumen requerido en metros cúbicos (m3)", example = "20.5") 
            @RequestParam("volumen") double volumen, 
            
            @Parameter(description = "Peso total de la carga en Kilogramos (kg)", example = "5000") 
            @RequestParam("peso") double peso) {
        
        log.info("[FLOTA] [BUSQUEDA_APTOS] Buscando camiones. Requerido -> Volumen: {}, Peso: {}", volumen, peso);

        CamionesAptosResponseDTO response = camionService.obtenerCamionesAptos(volumen, peso);
        
        log.info("[FLOTA] [BUSQUEDA_APTOS] Encontrados: {} camiones disponibles.", 
                 (response.getCamiones() != null ? response.getCamiones().size() : 0));
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Crear un nuevo camión", description = "Registra una nueva unidad en la flota. Requiere permisos administrativos o de operación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Camión creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en el cuerpo de la petición"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear activos")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<CamionResponseDTO> crearCamion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos técnicos del nuevo camión", required = true)
            @RequestBody CamionCreateRequestDTO request) {
        
        log.info("[FLOTA] [CREAR_CAMION] Solicitud de alta para Patente: {}", request.getPatente());

        CamionResponseDTO response = camionService.crearCamion(request);
        
        log.info("[FLOTA] [CREAR_CAMION] Camión creado exitosamente.");
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Obtener camión por patente", description = "Busca el detalle técnico y estado de un camión específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Camión encontrado"),
        @ApiResponse(responseCode = "404", description = "No se encontró un camión con esa patente")
    })
    @GetMapping("/{patente}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'CHOFER')")
    public ResponseEntity<CamionResponseDTO> obtenerCamionPorPatente(
            @Parameter(description = "Patente del vehículo (sin espacios ni guiones)", example = "AB123CD")
            @PathVariable String patente) {
        
        log.info("[FLOTA] [CONSULTA_CAMION] Buscando detalle de Patente: {}", patente);

        CamionResponseDTO response = camionService.obtenerCamionPorPatente(patente);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cambiar disponibilidad", description = "Permite marcar un camión como DISPONIBLE, EN_MANTENIMIENTO, etc.")
    @PutMapping("/{patente}/disponibilidad/{disponibilidad}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'CHOFER')")
    public ResponseEntity<Void> cambiarDisponibilidadCamion(
            @Parameter(description = "Patente del vehículo", example = "AB123CD")
            @PathVariable String patente,
            @Parameter(description = "Nuevo estado de disponibilidad", example = "MANTENIMIENTO")
            @PathVariable String disponibilidad) {
        
        log.info("[FLOTA] [ESTADO_CAMION] Cambiando estado. Patente: {} -> Nuevo Estado: {}", patente, disponibilidad);

        camionService.cambiarDisponibilidadCamion(patente, disponibilidad);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizar datos del camión", description = "Modifica características físicas o administrativas del vehículo.")
    @PutMapping("/{patente}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'CHOFER')")
    public ResponseEntity<Camion> actualizarCamion(
            @Parameter(description = "Patente del vehículo a modificar", example = "AB123CD")
            @PathVariable String patente, 
            @RequestBody CamionesActualizarDTO camionDTO) {
        
        log.info("[FLOTA] [ACTUALIZAR_CAMION] Modificando datos para Patente: {}", patente);

        Camion actualizado = camionService.actualizarCamion(patente, camionDTO);
        return ResponseEntity.ok(actualizado);
    }
}