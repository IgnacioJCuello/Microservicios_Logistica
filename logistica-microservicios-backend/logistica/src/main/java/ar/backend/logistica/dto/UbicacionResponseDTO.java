package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información detallada de una ubicación geográfica, incluyendo coordenadas.")
public class UbicacionResponseDTO {

    @Schema(description = "Identificador único de la ubicación", example = "501")
    private int idUbicacion;

    @Schema(description = "Dirección en formato texto legible", example = "Av. General Paz 120, Córdoba")
    private String direccion;

    @Schema(description = "Coordenada de latitud geográfica", example = "-31.4201")
    private Double latitud;

    @Schema(description = "Coordenada de longitud geográfica", example = "-64.1888")
    private Double longitud;
}