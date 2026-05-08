package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Desglose detallado de los costos finales y tiempo real de un envío entregado")
public class CostoRealDTO {
    
    @Schema(description = "ID de la solicitud finalizada", example = "100")
    private int idSolicitud;
    
    @Schema(description = "Costo calculado en base al volumen del contenedor", example = "15000.00")
    private Double costoBaseVolumen;
    
    @Schema(description = "Costo base del transporte (tarifa del camión x km recorridos)", example = "45000.50")
    private Double costoRecorridoBase;
    
    @Schema(description = "Costo total del combustible consumido en el trayecto", example = "12500.00")
    private Double costoCombustible;
    
    @Schema(description = "Costo acumulado por días de almacenamiento en depósitos intermedios", example = "5000.00")
    private Double costoEstadia;
    
    @Schema(description = "Cargos administrativos fijos por gestión", example = "2500.00")
    private Double costoGestion;
    
    @Schema(description = "Suma total final a facturar", example = "80000.50")
    private Double costoTotal;
    
    @Schema(description = "Tiempo total transcurrido en minutos desde el inicio hasta la entrega", example = "345")
    private Integer tiempoRealMinutos;
}