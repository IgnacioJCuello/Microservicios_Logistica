package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Confirmación de la asignación de un camión a un tramo específico")
public class TramoCamionResponseDTO {
    
    @Schema(description = "ID del tramo que fue modificado", example = "50")
    private int idTramo;
    
    @Schema(description = "Patente del vehículo que se vinculó al tramo", example = "AB123CD")
    private String patenteCamion;
    
    @Schema(description = "Nuevo estado del tramo tras la asignación", example = "ASIGNADO")
    private String estadoTramo;
}