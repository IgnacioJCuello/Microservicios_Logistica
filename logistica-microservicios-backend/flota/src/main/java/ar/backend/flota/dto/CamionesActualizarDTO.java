package ar.backend.flota.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CamionesActualizarDTO {

    @Schema(description = "Nueva capacidad de carga en peso (Kg)", example = "35000.0")
    private Double capacidadPeso;

    @Schema(description = "Nueva capacidad de carga en volumen (m3)", example = "45.5")
    private Double capacidadVolumen;

    @Schema(description = "Consumo promedio de combustible (Litros por Km)", example = "0.35")
    private Double consumoCombustibleXKilometro;

    @Schema(description = "Estado operativo del camión (DISPONIBLE, MANTENIMIENTO, EN_USO)", example = "MANTENIMIENTO")
    private String disponibilidad;
}