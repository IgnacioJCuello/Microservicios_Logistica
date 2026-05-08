package ar.backend.logistica.dto;

import java.time.Duration;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO que representa una sugerencia de tramo logístico con estimaciones de tiempo, distancia y ubicaciones.")
public class TramoSugeridoDTO {

    @Schema(description = "Identificador único del tramo", example = "105")
    private int idTramo;

    @Schema(description = "Número de orden del tramo dentro de la ruta global", example = "1")
    private int numeroOrden;

    @Schema(description = "Información detallada de la ubicación de origen")
    private UbicacionResponseDTO origen;

    @Schema(description = "Información detallada de la ubicación de destino")
    private UbicacionResponseDTO destino;

    @Schema(description = "Tipo de tramo (ej. CARRETERA, AEREO)", example = "CARRETERA")
    private String tipoTramo;

    @Schema(description = "Distancia estimada del recorrido en kilómetros", example = "120.5")
    private double distanciaEstimadaKm;

    @Schema(description = "Duración estimada del recorrido en formato ISO-8601 (PTnHnMnS)", example = "PT2H30M", type = "string")
    private Duration duracionEstimada;

    @Schema(description = "Fecha y hora estimada de inicio del tramo", example = "2025-11-20T08:00:00")
    private LocalDateTime fechaHoraInicioEstimada;

    @Schema(description = "Fecha y hora estimada de finalización del tramo", example = "2025-11-20T10:30:00")
    private LocalDateTime fechaHoraFinEstimada;

    @Schema(description = "Estado actual del tramo sugerido", example = "PENDIENTE")
    private String estadoTramo;
}