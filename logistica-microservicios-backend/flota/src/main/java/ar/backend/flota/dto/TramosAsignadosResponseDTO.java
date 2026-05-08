package ar.backend.flota.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta que agrupa todos los tramos asignados a un transportista.")
public class TramosAsignadosResponseDTO {

    @Schema(description = "Lista detallada de los tramos de viaje pendientes o en curso")
    List<TramoAsignadoResponseDTO> tramosAsignados;
}