package ar.backend.flota.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Respuesta tras la creación exitosa de un transportista.")
public class CreateTransportistaResponseDTO {

    @Schema(description = "ID único generado por el sistema", example = "15")
    private int idTransportista;

    @Schema(description = "Nombre del chofer registrado", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del chofer registrado", example = "Perez")
    private String apellido;

    @Schema(description = "Teléfono de contacto registrado", example = "3515559999")
    private String telefono;
}