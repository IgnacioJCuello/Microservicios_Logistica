package ar.backend.flota.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO que representa la información completa de un transportista")
public class TransportistaDTO {

    @Schema(description = "Identificador único del transportista", example = "101")
    private int idTransportista;

    @Schema(description = "Nombre del transportista", example = "Roberto Gómez")
    private String nombre;

    @Schema(description = "Lista de identificadores de los tramos asignados al transportista", example = "[5, 12, 44]")
    private List<Integer> tramosAsignados; 
}