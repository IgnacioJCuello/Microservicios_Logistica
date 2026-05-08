package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle de la configuración de tarifas")
public class ParametroTarifaResponseDTO {
    
    @Schema(description = "Identificador único de la configuración de tarifa", example = "1")
    private int idParametroTarifa;
    
    @Schema(description = "Precio del combustible configurado", example = "1250.50")
    private double precioCombustible;
    
    @Schema(description = "Cargo de gestión configurado", example = "5000.00")
    private double cargoGestion;
}