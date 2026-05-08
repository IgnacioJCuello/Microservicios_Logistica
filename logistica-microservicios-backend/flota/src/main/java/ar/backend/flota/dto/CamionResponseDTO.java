package ar.backend.flota.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Objeto de respuesta con los detalles técnicos y estado de un camión.")
public class CamionResponseDTO {

    @Schema(description = "Patente única del vehículo", example = "AA123BB")
    private String patente; 

    @Schema(description = "Consumo estimado de combustible (Litros por Km)", example = "0.45")
    private double consumoCombustibleXKilometro;

    @Schema(description = "Capacidad de carga volumétrica (metros cúbicos)", example = "40.5")
    private double capacidadVolumen;

    @Schema(description = "Capacidad máxima de carga en peso (Kilogramos)", example = "30000.0")
    private double capacidadPeso;

    @Schema(description = "Estado actual de operatividad (DISPONIBLE, EN_MANTENIMIENTO, EN_VIAJE)", example = "DISPONIBLE")
    private String disponibilidad;
}