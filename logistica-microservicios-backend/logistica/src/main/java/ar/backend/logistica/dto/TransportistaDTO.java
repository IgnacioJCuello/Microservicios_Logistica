package ar.backend.logistica.dto;

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
@Schema(description = "Información del transportista y sus asignaciones logísticas.")
public class TransportistaDTO {

    @Schema(description = "Identificador único del transportista", example = "10")
    private int idTransportista;

    @Schema(description = "Nombre del transportista o empresa de transporte", example = "Transportes El Rápido S.A.")
    private String nombre;

    @Schema(description = "Lista de identificadores de los tramos asignados al transportista", example = "[101, 205, 309]")
    private List<Integer> tramosAsignados; 
}
