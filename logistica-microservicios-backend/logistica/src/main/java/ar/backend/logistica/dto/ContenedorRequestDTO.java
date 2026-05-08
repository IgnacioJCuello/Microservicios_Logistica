package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos físicos requeridos para registrar un nuevo contenedor")
public class ContenedorRequestDTO {
    
    @Schema(description = "Peso total de la carga en Kilogramos (kg)", example = "2500.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private double peso;
    
    @Schema(description = "Volumen de la carga en Metros Cúbicos (m3)", example = "32.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private double volumen;
}