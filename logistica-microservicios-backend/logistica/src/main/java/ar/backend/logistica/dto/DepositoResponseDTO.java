package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Información completa de un depósito registrado")
public class DepositoResponseDTO {
    
    @Schema(description = "Identificador único del depósito", example = "1")
    private int idDeposito;
    
    @Schema(description = "Nombre del depósito", example = "Depósito Central Córdoba")
    private String nombre;
    
    @Schema(description = "Costo diario de estadía", example = "150.50")
    private Double costoEstadiaDiario;
    
    @Schema(description = "ID de la ubicación geográfica asociada", example = "10")
    private Integer idUbicacion;
}