package ar.backend.logistica.models.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeración que define los posibles estados del ciclo de vida de un contenedor en el flujo logístico.")
public enum EstadoContenedor {
    CREADO,
    EN_ORIGEN,
    EN_DEPOSITO,
    EN_TRANSITO,
    ENTREGADO
}