package ar.backend.logistica.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.backend.logistica.dto.AsignacionCamionDTO;
import ar.backend.logistica.dto.TramoCamionResponseDTO;
import ar.backend.logistica.dto.TramoConFechaFinReal;
import ar.backend.logistica.dto.TramoConFechaInicioReal;
import ar.backend.logistica.dto.TramoTransportistaResponseDTO;
import ar.backend.logistica.dto.TramosAsignadosResponseDTO;
import ar.backend.logistica.service.TramoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Importar Lombok

@Slf4j // Habilitar logs
@RestController
@RequestMapping("/tramos")
@RequiredArgsConstructor
@Tag(name = "Gestión de Tramos", description = "Endpoints para el seguimiento operativo (inicio/fin) y asignación de recursos en los tramos de viaje.")
public class TramoController {

    private final TramoService tramoService;

    @Operation(summary = "Iniciar tramo de viaje", description = "Marca el inicio real de un tramo. Acción realizada típicamente por el Chofer al comenzar el recorrido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tramo iniciado correctamente"),
        @ApiResponse(responseCode = "404", description = "Tramo no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado (Requiere ser CHOFER o ADMIN)")
    })
    @PostMapping("/{idTramo}/iniciar")
    @PreAuthorize("hasAnyRole('CHOFER', 'ADMIN')")
    public ResponseEntity<TramoConFechaInicioReal> iniciarTramo(
            @Parameter(description = "ID del tramo a iniciar", example = "50")
            @PathVariable int idTramo) {
        
        log.info("[LOGISTICA] [TRAMO_INICIO] Reporte de inicio recibido. TramoID: {}", idTramo);
        
        TramoConFechaInicioReal tramoActualizado = tramoService.iniciarTramo(idTramo);
        
        log.info("[LOGISTICA] [TRAMO_INICIO] Tramo iniciado exitosamente. Fecha Real: {}", tramoActualizado.getFechaHoraInicioReal());
        return ResponseEntity.ok(tramoActualizado);
    }

    @Operation(summary = "Finalizar tramo de viaje", description = "Marca la finalización real de un tramo. Acción realizada por el Chofer al llegar al destino intermedio o final.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tramo finalizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Tramo no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @PostMapping("/{idTramo}/finalizar")
    @PreAuthorize("hasAnyRole('CHOFER', 'ADMIN')")
    public ResponseEntity<TramoConFechaFinReal> finalizarTramo(
            @Parameter(description = "ID del tramo a finalizar", example = "50")
            @PathVariable int idTramo) {
        
        log.info("[LOGISTICA] [TRAMO_FIN] Reporte de fin recibido. TramoID: {}", idTramo);
        
        TramoConFechaFinReal tramoActualizado = tramoService.finalizarTramo(idTramo);
        
        log.info("[LOGISTICA] [TRAMO_FIN] Tramo finalizado exitosamente. Fecha Real: {}", tramoActualizado.getFechaHoraFinReal());
        return ResponseEntity.ok(tramoActualizado);
    }

    @Operation(summary = "Asignar camión a tramo", description = "Vincula un vehículo específico a un tramo de la ruta. Tarea logística de oficina.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Camión asignado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tramo o Camión no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado (Requiere rol OPERADOR o ADMIN)")
    })
    @PostMapping("/asignar-camion")
    @PreAuthorize("hasAnyRole('OPERADOR', 'ADMIN')")
    public ResponseEntity<TramoCamionResponseDTO> asignarCamion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID del tramo y patente del camión", required = true)
            @RequestBody AsignacionCamionDTO request) {
        
        log.info("[LOGISTICA] [ASIGNAR_CAMION] Vinculando Patente: {} al TramoID: {}", request.getPatenteCamion(), request.getIdTramo());
        
        TramoCamionResponseDTO response =
                tramoService.asignarCamion(request.getIdTramo(), request.getPatenteCamion());
        
        log.info("[LOGISTICA] [ASIGNAR_CAMION] Camión asignado correctamente.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Asignar transportista a tramo", description = "Vincula un conductor específico a un tramo. Tarea logística de oficina.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transportista asignado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tramo o Transportista no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto: El transportista ya tiene viaje en ese horario")
    })
    @PostMapping("/{idTramo}/asignar-transportista/{idTransportista}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'ADMIN')")
    public ResponseEntity<TramoTransportistaResponseDTO> asignarTransportista(
            @Parameter(description = "ID del tramo", example = "50")
            @PathVariable int idTramo,
            @Parameter(description = "ID del transportista", example = "10")
            @PathVariable int idTransportista) {
        
        log.info("[LOGISTICA] [ASIGNAR_CHOFER] Vinculando ChoferID: {} al TramoID: {}", idTransportista, idTramo);
        
        TramoTransportistaResponseDTO response =
                tramoService.asignarTransportista(idTramo, idTransportista);
        
        log.info("[LOGISTICA] [ASIGNAR_CHOFER] Chofer asignado correctamente.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar estado de tramos asignados", description = "Permite monitorear el estado actual de una lista específica de tramos.")
    @PostMapping("/asignados")
    @PreAuthorize("hasAnyRole('ADMIN', 'CHOFER')")
    public ResponseEntity<TramosAsignadosResponseDTO> obtenerTramosAsignados(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Lista de IDs de tramos a consultar", required = true)
            @RequestBody List<Integer> idsTramos) {
        
        log.info("[LOGISTICA] [CONSULTA_ASIGNADOS] Consultando estado de {} tramos.", idsTramos.size());
        
        return ResponseEntity.ok(
                tramoService.obtenerTramosAsignados(idsTramos)
        );
    }
}