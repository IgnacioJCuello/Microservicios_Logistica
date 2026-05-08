package ar.backend.logistica.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto contenedor que agrupa la lista de tramos asignados.")
public class TramosAsignadosResponseDTO {

    @Schema(description = "Lista detallada de los tramos que han sido asignados")
    List<TramoAsignadoResponseDTO> tramosAsignados;
}