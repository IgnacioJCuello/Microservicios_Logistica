package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO que representa la información técnica y estado de un camión")
public class CamionDTO {

    @Schema(description = "Patente (dominio) identificador del vehículo", example = "AA456BB")
    private String patente;

    @Schema(description = "Consumo promedio de combustible por kilómetro (en litros)", example = "0.35")
    private double consumoCombustibleXKilometro;

    @Schema(description = "Capacidad total de volumen de carga (m³)", example = "85.0")
    private double capacidadVolumen;

    @Schema(description = "Capacidad máxima de peso de carga (kg)", example = "28000.0")
    private double capacidadPeso;

    @Schema(description = "Estado actual de disponibilidad del camión (ej: DISPONIBLE, EN_MANTENIMIENTO)", example = "DISPONIBLE")
    private String disponibilidad;
}