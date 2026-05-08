package ar.backend.logistica.models.enums;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Define el estado actual de una solicitud de envío dentro del proceso logístico.")
public enum EstadoTramo {
    PROGRAMADO,  // La ruta se creó, pero el tramo no tiene camión
    ASIGNADO,    // El tramo ya tiene un camión 
    EN_TRANSITO, // El transportista marcó inicio
    FINALIZADO   // El transportista marcó fin
}