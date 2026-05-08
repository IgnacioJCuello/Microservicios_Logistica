package ar.backend.flota.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Respuesta que contiene la lista de camiones que cumplen con los requisitos de peso y volumen solicitados.")
public class CamionesAptosResponseDTO {

    @Schema(description = "Lista de camiones disponibles y aptos para la carga")
    private List<CamionResponseDTO> camiones;
}