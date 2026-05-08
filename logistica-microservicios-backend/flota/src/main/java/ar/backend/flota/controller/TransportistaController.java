package ar.backend.flota.controller;

import ar.backend.flota.dto.AsignarTramoTransportistaRequestDTO;
import ar.backend.flota.dto.TramosAsignadosResponseDTO;
import ar.backend.flota.dto.TransportistaDTO;
import ar.backend.flota.dto.CreateTransportistaRequestDTO;
import ar.backend.flota.dto.CreateTransportistaResponseDTO;
import ar.backend.flota.models.Transportista;
import ar.backend.flota.service.TransportistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transportistas")
@Tag(name = "Gestión de Transportistas", description = "Endpoints para la administración de choferes y asignación de viajes.")
public class TransportistaController {

    private final TransportistaService transportistaService;

    @Autowired
    public TransportistaController(TransportistaService transportistaService) {
        this.transportistaService = transportistaService;
    }

    @Operation(summary = "Listar todos los transportistas", description = "Devuelve el listado completo de empleados. Requiere rol ADMIN u OPERADOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos para ver el listado")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<List<Transportista>> getAll() {
        return ResponseEntity.ok(transportistaService.getAllTransportistas());
    }

    @Operation(summary = "Registrar nuevo transportista", description = "Da de alta un nuevo chofer en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transportista creado exitosamente"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear transportistas")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<CreateTransportistaResponseDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del nuevo transportista", required = true)
            @RequestBody CreateTransportistaRequestDTO transportista) {
        
        CreateTransportistaResponseDTO nuevo = transportistaService.createTransportista(transportista);
        return ResponseEntity.ok(nuevo);
    }

    @Operation(summary = "Obtener transportista por ID", description = "Busca el detalle de un chofer específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transportista encontrado"),
        @ApiResponse(responseCode = "404", description = "Transportista no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<TransportistaDTO> obtenerTransportista(
            @Parameter(description = "ID del transportista", example = "1") 
            @PathVariable int id) {
        return ResponseEntity.ok(transportistaService.obtenerTransportistaPorId(id));
    }

    @Operation(summary = "Asignar tramo", description = "Vincula un chofer a un tramo de viaje específico. Tarea operativa.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Asignación exitosa"),
        @ApiResponse(responseCode = "400", description = "Error en los datos de asignación")
    })
    @PostMapping("/asignar-tramo")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<Void> asignarTramo(
            @RequestBody AsignarTramoTransportistaRequestDTO dto) {
        transportistaService.asignarTramo(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Ver tramos asignados", description = "Permite al chofer ver su hoja de ruta o a la administración consultar asignaciones.")
    @GetMapping("/{idTransportista}/tramos-asignados")
    @PreAuthorize("hasAnyRole('ADMIN', 'CHOFER')")
    public ResponseEntity<TramosAsignadosResponseDTO> obtenerTramosAsignados(
            @Parameter(description = "ID del transportista", example = "5")
            @PathVariable int idTransportista) {

        TramosAsignadosResponseDTO response =
                transportistaService.obtenerTramosAsignados(idTransportista);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Desasignar tramo", description = "Libera al chofer de un tramo asignado.")
    @PutMapping("{idTransportista}/desasignar-tramo/{idTramo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'CHOFER')")
    public ResponseEntity<Void> desasignarTramo(
        @Parameter(description = "ID del transportista", example = "5")
        @PathVariable int idTransportista,
        @Parameter(description = "ID del tramo a desasignar", example = "102")
        @PathVariable int idTramo) {
            transportistaService.desasignarTramo(idTransportista, idTramo);
            return ResponseEntity.ok().build();
        }
}