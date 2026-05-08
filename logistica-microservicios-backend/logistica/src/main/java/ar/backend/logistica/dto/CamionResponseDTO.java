package ar.backend.logistica.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de respuesta que agrupa un listado de camiones")
public class CamionResponseDTO {

    @Schema(description = "Lista de objetos con la información detallada de los camiones")
    private List<CamionDTO> camiones;
}