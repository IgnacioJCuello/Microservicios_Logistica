package ar.backend.logistica.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.backend.logistica.dto.RutaTentativaRequestDTO;
import ar.backend.logistica.dto.RutaTentativaResponse;
import ar.backend.logistica.dto.RutasTentativasResponse;
import ar.backend.logistica.dto.SolicitudConRutaDTO;
import ar.backend.logistica.dto.SolicitudCreateRequest;
import ar.backend.logistica.dto.SolicitudResponseDTO;
import ar.backend.logistica.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ar.backend.logistica.dto.AsignacionRutaDTO;

@Slf4j
@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
@Tag(name = "Gestión de Solicitudes", description = "Endpoints para la creación, planificación y asignación de envíos de contenedores.")
public class SolicitudController {
    
    private final SolicitudService solicitudService;

    // ALTA: El Cliente crea su pedido.
    @Operation(summary = "Registrar nueva solicitud", description = "Permite a un cliente iniciar un pedido de traslado. Crea el contenedor, valida ubicaciones y genera el registro inicial.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado (Solo CLIENTE o ADMIN)")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<SolicitudResponseDTO> crearSolicitud(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Detalles del envío (cliente, origen, destino, carga)", required = true)
        @RequestBody SolicitudCreateRequest request) {

        log.info("[LOGISTICA] [SOLICITUD] Recibida petición de creación. Cliente: {}", request.getCliente().getEmail());
        
        SolicitudResponseDTO response = solicitudService.crearSolicitud(request);
        
        log.info("[LOGISTICA] [SOLICITUD] Solicitud creada con ID: {}", response.getIdSolicitud());
        return ResponseEntity.ok(response);
    }

    // OPERATIVA: Crear una nueva ruta posible
    @Operation(summary = "Generar ruta tentativa", description = "Permite a un operador planificar una posible ruta (tramos) para una solicitud y calcular sus costos estimados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ruta tentativa creada y calculada"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
        @ApiResponse(responseCode = "403", description = "No autorizado (Solo OPERADOR o ADMIN)")
    })
    @PostMapping("/rutas-tentativas/{idSolicitud}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'ADMIN')")
    public ResponseEntity<RutaTentativaResponse> crearRutaTentativa(
        @Parameter(description = "ID de la solicitud a planificar", example = "1")
        @PathVariable int idSolicitud,
        
        @RequestBody RutaTentativaRequestDTO request) {
        
        log.info("[LOGISTICA] [RUTA_TENTATIVA] Generando ruta para SolicitudID: {}", idSolicitud);
        
        RutaTentativaResponse response = solicitudService.crearRutaTentativa(idSolicitud, request);
        
        log.info("[LOGISTICA] [RUTA_TENTATIVA] Ruta generada. Costo Estimado: {}", response.getCostoEstimado());
        return ResponseEntity.ok(response);
    }

    // CONSULTA: Ver las opciones de ruta.
    @Operation(summary = "Listar rutas tentativas", description = "Muestra todas las opciones de ruta que se han planificado para una solicitud específica.")
    @GetMapping("/rutas-tentativas/{idSolicitud}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'ADMIN')")
    public ResponseEntity<RutasTentativasResponse> listarRutasTentativas(
        @Parameter(description = "ID de la solicitud", example = "1")
        @PathVariable int idSolicitud) {
            
            log.info("[LOGISTICA] [LISTAR_RUTAS] Solicitando rutas para SolicitudID: {}", idSolicitud);
            
            RutasTentativasResponse response = solicitudService.listarRutasTentativas(idSolicitud);
            
            log.info("[LOGISTICA] [LISTAR_RUTAS] Respuesta enviada.");
            return ResponseEntity.ok(response);
    }

    // OPERATIVA: Confirmar la ruta definitiva y asignar recursos.
    @Operation(summary = "Asignar ruta definitiva", description = "Confirma una de las rutas tentativas como la oficial para el envío. Cambia el estado de la solicitud a PROGRAMADA.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ruta asignada correctamente"),
        @ApiResponse(responseCode = "400", description = "ID de ruta o solicitud inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado (Solo OPERADOR o ADMIN)")
    })
    @PostMapping("/rutas-tentativas/asignar")
    @PreAuthorize("hasAnyRole('OPERADOR', 'ADMIN')")
    public ResponseEntity<SolicitudConRutaDTO> asignarRuta(
            @RequestBody AsignacionRutaDTO asignacion) {
        
        log.info("[LOGISTICA] [ASIGNAR_RUTA] Recibida petición de asignación. SolicitudID: {}", asignacion.getIdSolicitud());
        
        SolicitudConRutaDTO solicitudConRuta = solicitudService.asignarRutaASolicitud(asignacion);
        
        log.info("[LOGISTICA] [ASIGNAR_RUTA] Ruta asignada. Estado actual: {}", solicitudConRuta.getEstado());
        return ResponseEntity.ok(solicitudConRuta);
    } 
}