package ar.backend.flota.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la creación de un nuevo camión en la flota")
public class CamionCreateRequestDTO {

    @Schema(description = "Patente única del camión", example = "ABC-123")
    private String patente;

    @Schema(description = "Capacidad máxima de carga en kilogramos", example = "5000.0")
    private Double capacidadPeso;

    @Schema(description = "Capacidad máxima de volumen en metros cúbicos", example = "25.5")
    private Double capacidadVolumen;

    @Schema(description = "Consumo de combustible en litros por kilómetro recorrido", example = "0.15")
    private Double consumoCombustibleXKilometro;

    @Schema(description = "Estado de disponibilidad del camión", example = "DISPONIBLE")
    private String disponibilidad;
}