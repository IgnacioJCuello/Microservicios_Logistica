package ar.backend.logistica.models.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Define el estado actual de una solicitud de envío dentro del proceso logístico.")
public enum EstadoSolicitud {
    BORRADOR,
    PROGRAMADA,
    EN_TRANSITO,
    ENTREGADA
}