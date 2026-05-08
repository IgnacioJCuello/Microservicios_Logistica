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
@Schema(description = "DTO de solicitud para asignar un tramo específico a un transportista")
public class AsignarTramoTransportistaRequestDTO {

    @Schema(description = "Identificador del transportista al que se le asignará el tramo", example = "101")
    private int idTransportista;

    @Schema(description = "Identificador del tramo logístico a asignar", example = "45")
    private int idTramo;
}