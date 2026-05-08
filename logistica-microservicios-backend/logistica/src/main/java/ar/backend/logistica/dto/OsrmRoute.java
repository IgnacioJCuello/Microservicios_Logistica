package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO que representa la distancia y duración básica obtenida de un motor de ruteo (OSRM)")
public class OsrmRoute {

    @Schema(description = "Distancia total de la ruta (en metros)", example = "15000.0")
    private double distance;

    @Schema(description = "Duración estimada del viaje (en segundos)", example = "900.0")
    private double duration;
}