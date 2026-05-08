package ar.backend.flota.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AsignarTramoTransportistaRequestDTO {

    @Schema(description = "Identificador único del transportista (Chofer)", example = "5")
    private int idTransportista;

    @Schema(description = "Identificador único del tramo de viaje a asignar", example = "102")
    private int idTramo;
}
