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
@Schema(description = "Versión simplificada de una ubicación geográfica, utilizada para referencias rápidas.")
public class UbicacionSimpleDTO {

    @Schema(description = "Identificador único de la ubicación", example = "204")
    private int idUbicacion;

    @Schema(description = "Dirección física o referencia del lugar", example = "Puerto de Buenos Aires, Terminal 4")
    private String direccion;
}