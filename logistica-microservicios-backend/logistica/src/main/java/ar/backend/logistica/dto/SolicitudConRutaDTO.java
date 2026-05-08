package ar.backend.logistica.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Detalle completo de una solicitud con su ruta asignada")
public class SolicitudConRutaDTO {
    
    @Schema(description = "Identificador único de la solicitud", example = "100")
    private int idSolicitud;
    
    @Schema(description = "ID del cliente solicitante", example = "5")
    private int idCliente;
    
    @Schema(description = "ID del contenedor asociado", example = "123")
    private int idContenedor;
    
    @Schema(description = "ID de la ubicación de origen", example = "10")
    private int ubicacionOrigen;
    
    @Schema(description = "ID de la ubicación de destino", example = "20")
    private int ubicacionDestino;
    
    @Schema(description = "ID de la configuración de tarifa aplicada", example = "1")
    private int idParametroTarifa;
    
    @Schema(description = "Fecha y hora de creación del pedido")
    private LocalDateTime fechaHoraCreacion;
    
    @Schema(description = "Estado actual del envío", example = "PROGRAMADA")
    private String estado;
    
    @Schema(description = "Costo total calculado para el envío", example = "150000.00")
    private double costoEstimado;
    
    @Schema(description = "Tiempo total estimado de viaje (en minutos)", example = "240.0")
    private double tiempoEstimado;
    
    @Schema(description = "ID de la ruta seleccionada para este envío", example = "50")
    private int rutaAsignada;
}