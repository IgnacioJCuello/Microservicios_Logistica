package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Respuesta que contiene la información de un tramo, su estado y el transportista asignado.")
public class TramoTransportistaResponseDTO {

    @Schema(description = "Identificador único del tramo logístico", example = "123")
    private int idTramo;

    @Schema(description = "Estado actual del tramo", example = "EN_TRANSITO")
    private String estadoTramo;

    @Schema(description = "Identificador del transportista asignado al tramo", example = "15")
    private int idTransportista;
}
