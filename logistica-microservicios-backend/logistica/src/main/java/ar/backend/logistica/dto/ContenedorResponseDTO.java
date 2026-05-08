package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta con la información detallada de un contenedor de carga")
public class ContenedorResponseDTO {

    @Schema(description = "Identificador único del contenedor", example = "889")
    private int idContenedor;

    @Schema(description = "Peso del contenedor o de la carga (en kg)", example = "2500.50")
    private double peso;

    @Schema(description = "Volumen del contenedor (en m³)", example = "33.2")
    private double volumen;

    @Schema(description = "Estado operativo del contenedor (ej: VACIO, CARGADO, EN_TRANSITO)", example = "CARGADO")
    private String estado;
}