package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Datos para registrar o buscar una ubicación geográfica.")
public class UbicacionRequestDTO {

    @Schema(description = "Dirección física (Calle y número)", example = "Av. Colon 5000, Cordoba")
    private String direccion;

    @Schema(description = "Coordenada de latitud en formato decimal", example = "-31.4135")
    private Double latitud;

    @Schema(description = "Coordenada de longitud en formato decimal", example = "-64.1810")
    private Double longitud;
}