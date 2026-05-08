package ar.backend.logistica.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resumen de la solicitud creada")
public class SolicitudResponseDTO {
    
    @Schema(description = "Identificador generado para la solicitud", example = "100")
    private int idSolicitud;
    
    @Schema(description = "Estado inicial de la solicitud", example = "PENDIENTE")
    private String estadoSolicitud;
    
    @Schema(description = "Fecha y hora de registro")
    private LocalDateTime fechaHoraCreacion;
    
    @Schema(description = "Información del cliente registrado")
    private ClienteResponseDTO cliente;
    
    @Schema(description = "Detalle del punto de origen")
    private UbicacionResponseDTO ubicacionOrigen;
    
    @Schema(description = "Detalle del punto de destino")
    private UbicacionResponseDTO ubicacionDestino;
    
    @Schema(description = "Tarifa aplicada al envío")
    private ParametroTarifaResponseDTO parametroTarifa;
    
    @Schema(description = "ID del contenedor vinculado", example = "123")
    private int idContenedor;
}