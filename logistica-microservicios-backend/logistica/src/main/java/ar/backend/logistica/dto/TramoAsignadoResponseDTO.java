package ar.backend.logistica.dto;

import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Detalle completo de un tramo logístico perteneciente a una ruta")
public class TramoAsignadoResponseDTO {

    @Schema(description = "Identificador único del tramo", example = "50")
    private int idTramo;

    @Schema(description = "Identificador de la ruta a la que pertenece este tramo", example = "10")
    private int idRuta;

    @Schema(description = "Información resumida de la ubicación de partida")
    private UbicacionSimpleDTO origen;

    @Schema(description = "Información resumida de la ubicación de llegada")
    private UbicacionSimpleDTO destino;

    @Schema(description = "Patente del camión asignado para realizar este tramo (si ya fue asignado)", example = "AB123CD")
    private String patenteCamion;

    @Schema(description = "Clasificación del tramo (ej. ORIGEN_DEPOSITO, DEPOSITO_DESTINO)", example = "ORIGEN_DEPOSITO")
    private String tipoTramo;

    @Schema(description = "Posición secuencial de este tramo dentro de la ruta global", example = "1")
    private int numeroOrden;

    @Schema(description = "Distancia calculada para el tramo en Kilómetros", example = "120.5")
    private double distanciaEstimada;

    @Schema(description = "Fecha y hora planificada para el inicio del viaje")
    private LocalDateTime fechaHoraInicioEstimada;

    @Schema(description = "Fecha y hora planificada para la finalización del viaje")
    private LocalDateTime fechaHoraFinEstimada;
}