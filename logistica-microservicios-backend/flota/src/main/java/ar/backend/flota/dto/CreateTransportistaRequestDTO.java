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
@Schema(description = "DTO para la creación de un nuevo transportista (chofer)")
public class CreateTransportistaRequestDTO {
    
    @Schema(description = "Nombre del transportista", example = "Juan")
    private String nombre;
    
    @Schema(description = "Apellido del transportista", example = "Pérez")
    private String apellido;
    
    @Schema(description = "Número de teléfono de contacto del transportista", example = "+54911234567")
    private String telefono;
}