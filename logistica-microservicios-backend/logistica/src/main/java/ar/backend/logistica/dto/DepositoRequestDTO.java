package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Datos necesarios para registrar o modificar un depósito")
public class DepositoRequestDTO {
    
    @Schema(description = "Nombre comercial del depósito", example = "Depósito Central Córdoba")
    private String nombre;
    
    @Schema(description = "Costo por día de almacenamiento en este depósito", example = "150.50")
    private Double costoEstadiaDiario;
    
    @Schema(description = "ID de la ubicación física asociada (del microservicio de Ubicaciones)", example = "10")
    private Integer idUbicacion;
}