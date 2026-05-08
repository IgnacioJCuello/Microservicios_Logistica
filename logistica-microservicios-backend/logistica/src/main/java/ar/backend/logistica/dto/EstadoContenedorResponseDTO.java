package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO de respuesta que indica el estado actual de un contenedor específico")
public class EstadoContenedorResponseDTO {

    @Schema(description = "Identificador único del contenedor", example = "1024")
    private int idContenedor;

    @Schema(description = "Estado actual del contenedor (ej: EN_DEPOSITO, ENTREGADO)", example = "EN_DEPOSITO")
    private String estado;
}