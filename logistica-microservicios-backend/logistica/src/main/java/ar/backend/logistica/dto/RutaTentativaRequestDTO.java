package ar.backend.logistica.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de solicitud para crear o proponer una nueva ruta tentativa, compuesta por una secuencia de tramos.")
public class RutaTentativaRequestDTO {

    @Schema(description = "Identificador de la solicitud de envío a la que se asocia esta ruta", example = "300")
    private int idSolicitud;

    @Schema(description = "Lista de objetos 'TramoRequestDTO' que definen la secuencia de paradas y segmentos de la ruta.")
    private List<TramoRequestDTO> tramos;
}