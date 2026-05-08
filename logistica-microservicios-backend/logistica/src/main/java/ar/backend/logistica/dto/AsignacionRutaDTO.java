package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para confirmar la asignación de una ruta tentativa a una solicitud específica")
public class AsignacionRutaDTO{

    @Schema(description = "Identificador único de la solicitud de envío", example = "2045")
    private int idSolicitud;

    @Schema(description = "Identificador de la ruta tentativa seleccionada para cubrir la solicitud", example = "3")
    private int idRutaTentativa;
}