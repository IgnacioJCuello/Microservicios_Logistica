package ar.backend.logistica.dto;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO de respuesta que agrupa un listado de contenedores ubicados en un depósito.")
public class ContenedoresDepositoDTO {

    @Schema(description = "Lista de objetos 'ContenedorResponseDTO' con la información detallada de cada contenedor.")
    private List<ContenedorResponseDTO> contenedores;
    
}