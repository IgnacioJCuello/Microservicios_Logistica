package ar.backend.logistica.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta que detalla un tramo de ruta con su información de seguimiento y finalización real")
public class TramoConFechaFinReal {

    @Schema(description = "Identificador único del tramo", example = "45")
    private int idTramo;

    @Schema(description = "Número de secuencia del tramo dentro de la ruta", example = "3")
    private int numeroOrden;

    @Schema(description = "Identificador de la ubicación de origen", example = "10")
    private int idUbicacionOrigen;

    @Schema(description = "Identificador de la ubicación de destino", example = "15")
    private int idUbicacionDestino;

    @Schema(description = "Tipo de segmento logístico (ej: CARGA, DESCARGA, VIAJE)", example = "VIAJE")
    private String tipoTramo;

    @Schema(description = "Distancia estimada del tramo (en kilómetros)", example = "150.7")
    private double distanciaEstimadaKm;

    @Schema(description = "Fecha y hora real en que comenzó el tramo", example = "2025-11-19T10:00:00")
    private LocalDateTime fechaHoraInicioReal;

    @Schema(description = "Fecha y hora real en que finalizó el tramo", example = "2025-11-19T12:30:00")
    private LocalDateTime fechaHoraFinReal;

    @Schema(description = "Estado actual del tramo (ej: EN_CURSO, FINALIZADO)", example = "FINALIZADO")
    private String estadoTramo;

    @Schema(description = "Costo total final o estimado para el tramo (moneda local)", example = "12500.50")
    private Double costoTotal;
}