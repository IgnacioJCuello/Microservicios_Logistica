package ar.backend.logistica.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta que detalla una ruta tentativa propuesta, incluyendo métricas y tramos.")
public class RutaTentativaResponse {

    @Schema(description = "Identificador único de la ruta tentativa generada", example = "3")
    private int idRuta;

    @Schema(description = "Costo total estimado de la ruta (moneda local)", example = "150000.50")
    private double costoEstimado;

    @Schema(description = "Tiempo total estimado de viaje de la ruta (en horas)", example = "48.5")
    private double tiempoEstimado;

    @Schema(description = "Lista de objetos 'TramoSugeridoDTO' que componen la ruta propuesta.")
    private List<TramoSugeridoDTO> tramos;
}