package ar.backend.logistica.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para definir un tramo (segmento) específico de una ruta logística.")
public class TramoRequestDTO {

    @Schema(description = "Objeto DTO con los detalles de la ubicación de origen.")
    private UbicacionRequestDTO ubicacionOrigen;

    @Schema(description = "Objeto DTO con los detalles de la ubicación de destino.")
    private UbicacionRequestDTO ubicacionDestino;

    @Schema(description = "Tipo de segmento logístico (ej: VIAJE, CARGA, DESCARGA)", example = "VIAJE")
    private String tipoTramo;

    @Schema(description = "Número de secuencia del tramo dentro de la ruta (indica el orden)", example = "1")
    private int numeroOrden;

    @Schema(description = "Fecha y hora estimada en que comenzará el tramo (formato ISO 8601)", example = "2025-11-20T08:00:00")
    private LocalDateTime fechaHoraInicioEstimada;
}