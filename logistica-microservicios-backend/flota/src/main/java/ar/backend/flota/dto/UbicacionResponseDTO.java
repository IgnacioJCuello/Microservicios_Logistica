package ar.backend.flota.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Representación simplificada de una ubicación geográfica (Origen o Destino).")
public class UbicacionResponseDTO {

    @Schema(description = "Identificador único de la ubicación", example = "101")
    private int idUbicacion;

    @Schema(description = "Dirección física o nombre del punto de interés", example = "Av. Colon 5000, Cordoba")
    private String direccion;
}