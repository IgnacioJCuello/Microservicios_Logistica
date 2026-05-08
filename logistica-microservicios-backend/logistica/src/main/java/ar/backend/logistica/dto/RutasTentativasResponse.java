package ar.backend.logistica.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto contenedor que agrupa todas las opciones de rutas calculadas para una solicitud específica")
public class RutasTentativasResponse {
    
    @Schema(description = "Identificador de la solicitud para la cual se calcularon estas opciones", example = "100")
    private int idSolicitud;
    
    @Schema(description = "Lista de alternativas de ruta disponibles, cada una con sus propios costos y tiempos estimados")
    private List<RutaTentativaResponse> rutasTentativas;
}