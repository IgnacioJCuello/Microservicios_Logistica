package ar.backend.logistica.client;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import ar.backend.logistica.dto.CamionDTO;
import ar.backend.logistica.dto.CamionResponseDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CamionClient {
    private final WebClient.Builder webClientBuilder;

    public List<CamionDTO> obtenerCamionesAptos(double volumen, double peso) {
        
        CamionResponseDTO response = webClientBuilder.build()
            .get()
            .uri("http://flota/camiones/aptos?volumen={v}&peso={p}", volumen, peso)
            .retrieve()
            .bodyToMono(CamionResponseDTO.class) 
            .block();
        
        return response.getCamiones();
    }

    public CamionDTO obtenerCamionPorPatente(String patente) {
        CamionDTO camion = webClientBuilder.build()
            .get()
            .uri("http://flota/camiones/{patente}", patente)
            .retrieve()
            .bodyToMono(CamionDTO.class)
            .block();
        
        return camion;
    }

    public void cambiarDisponibilidadCamion(String patente, String disponibilidad) {

        webClientBuilder.build()
            .put()
            .uri("http://flota/camiones/{patente}/disponibilidad/{disp}",
                    patente, disponibilidad)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
