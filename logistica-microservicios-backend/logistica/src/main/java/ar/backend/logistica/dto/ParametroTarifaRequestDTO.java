package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Datos requeridos para definir o actualizar costos logísticos")
public class ParametroTarifaRequestDTO {
    
    @Schema(description = "Precio actual del litro de combustible", example = "1250.50")
    private double precioCombustible;
    
    @Schema(description = "Costo fijo administrativo por gestión de solicitud", example = "5000.00")
    private double cargoGestion;
}