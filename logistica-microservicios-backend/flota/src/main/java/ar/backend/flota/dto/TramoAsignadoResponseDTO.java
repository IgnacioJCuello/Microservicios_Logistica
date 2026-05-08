package ar.backend.flota.dto;

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
@Schema(description = "Detalle de un tramo asignado a un transportista.")
public class TramoAsignadoResponseDTO {

    @Schema(description = "Identificador único del tramo", example = "50")
    private int idTramo;

    @Schema(description = "Identificador de la ruta a la que pertenece el tramo", example = "10")
    private int idRuta;

    @Schema(description = "Ubicación de origen del tramo")
    private UbicacionResponseDTO origen;

    @Schema(description = "Ubicación de destino del tramo")
    private UbicacionResponseDTO destino;

    @Schema(description = "Patente del camión asignado", example = "AA123BB")
    private String patenteCamion;

    @Schema(description = "Tipo de tramo (CARRETERA, MARITIMO, AEREO)", example = "CARRETERA")
    private String tipoTramo;

    @Schema(description = "Orden secuencial del tramo dentro de la ruta", example = "1")
    private int numeroOrden;

    @Schema(description = "Distancia estimada en Kilómetros", example = "450.5")
    private double distanciaEstimada;

    @Schema(description = "Fecha y hora estimada de salida (ISO-8601)", example = "2023-11-20T08:00:00")
    private LocalDateTime fechaHoraInicioEstimada;

    @Schema(description = "Fecha y hora estimada de llegada (ISO-8601)", example = "2023-11-20T14:30:00")
    private LocalDateTime fechaHoraFinEstimada;
}