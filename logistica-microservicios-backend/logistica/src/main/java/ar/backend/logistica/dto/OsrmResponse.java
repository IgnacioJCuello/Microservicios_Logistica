package ar.backend.logistica.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Contenedor de la respuesta recibida desde la API de rutas OSRM")
public class OsrmResponse {
    
    @Schema(description = "Lista de rutas posibles calculadas entre los puntos")
    private List<OsrmRoute> routes;

    // Estos métodos son helpers lógicos, no necesitan @Schema 
    // a menos que este objeto se serialice hacia el frontend.
    public double getDistanceMeters() {
        if (routes != null && !routes.isEmpty()) {
            return routes.get(0).getDistance();
        }
        return 0.0;
    }

    public double getDurationSeconds() {
         if (routes != null && !routes.isEmpty()) {
            return routes.get(0).getDuration();
        }
        return 0.0;
    }
}