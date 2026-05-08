package ar.backend.logistica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta que representa los datos de un cliente registrado")
public class ClienteResponseDTO {

    @Schema(description = "Identificador único del cliente", example = "501")
    private int idCliente;

    @Schema(description = "Nombre de pila del cliente", example = "María")
    private String nombre;

    @Schema(description = "Apellido del cliente", example = "López")
    private String apellido;

    @Schema(description = "Correo electrónico de contacto", example = "maria.lopez@email.com")
    private String email;

    @Schema(description = "Número de teléfono de contacto", example = "+5491198765432")
    private String telefono;
}