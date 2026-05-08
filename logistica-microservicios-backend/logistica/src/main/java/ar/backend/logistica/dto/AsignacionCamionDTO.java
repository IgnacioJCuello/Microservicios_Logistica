package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO utilizado para asignar un camión específico a un tramo logístico")
public class AsignacionCamionDTO {

    @Schema(description = "Identificador único del tramo a asignar", example = "45")
    private int idTramo;

    @Schema(description = "Patente (dominio) del camión asignado", example = "AE123CD")
    private String patenteCamion;
}