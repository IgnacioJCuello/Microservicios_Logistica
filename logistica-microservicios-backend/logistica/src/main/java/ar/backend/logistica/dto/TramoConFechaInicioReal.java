package ar.backend.logistica.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de solicitud/respuesta que registra el inicio real de un tramo de ruta")
public class TramoConFechaInicioReal {

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

    @Schema(description = "Fecha y hora real en que comenzó el tramo (formato ISO 8601)", example = "2025-11-19T10:00:00")
    private LocalDateTime fechaHoraInicioReal;

    @Schema(description = "Estado actual del tramo (ej: EN_CURSO, PENDIENTE)", example = "EN_CURSO")
    private String estadoTramo;
}