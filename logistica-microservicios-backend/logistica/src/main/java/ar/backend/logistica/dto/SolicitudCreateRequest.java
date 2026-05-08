package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Formulario de creación de una nueva solicitud de envío")
public class SolicitudCreateRequest {
    
    @Schema(description = "Datos del cliente que solicita el envío")
    private ClienteRequestDTO cliente;
    
    @Schema(description = "Datos de la ubicación de retiro")
    private UbicacionRequestDTO ubicacionOrigen;
    
    @Schema(description = "Datos de la ubicación de entrega")
    private UbicacionRequestDTO ubicacionDestino;
    
    @Schema(description = "Parámetros de costos a aplicar (combustible, gestión)")
    private ParametroTarifaRequestDTO parametroTarifa;
    
    @Schema(description = "ID del contenedor a transportar (debe existir previamente)", example = "123")
    private int idContenedor;
}