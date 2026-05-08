package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "DTO con la información requerida para registrar o actualizar un cliente")
public class ClienteRequestDTO {

    @Schema(description = "Nombre de pila del cliente", example = "María")
    private String nombre;

    @Schema(description = "Apellido del cliente", example = "López")
    private String apellido;

    @Schema(description = "Correo electrónico de contacto", example = "maria.lopez@email.com")
    private String email;

    @Schema(description = "Número de teléfono de contacto", example = "+5491198765432")
    private String telefono;
}