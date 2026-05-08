package ar.backend.logistica.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import ar.backend.logistica.dto.AsignarTramoTransportistaRequestDTO;
import ar.backend.logistica.dto.TransportistaDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransportistaClient {

    private final WebClient.Builder webClientBuilder;

    public TransportistaDTO obtenerTransportistaPorId(int idTransportista) {
        return webClientBuilder.build()
                .get()
                .uri("http://flota/transportistas/{id}", idTransportista)
                .retrieve()
                .bodyToMono(TransportistaDTO.class)
                .block();
    }

    public void asignarTramoATransportista(int idTransportista, int idTramo) {

        AsignarTramoTransportistaRequestDTO body = new AsignarTramoTransportistaRequestDTO(idTransportista, idTramo);

        webClientBuilder.build()
                .post()
                .uri("http://flota/transportistas/asignar-tramo")
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void desasignarTramoATransportista(int idTransportista, int idTramo) {
        webClientBuilder.build()
            .put()
            .uri("http://flota/transportistas/{idTransportista}/desasignar-tramo/{idTramo}", idTransportista, idTramo )
            .retrieve()
            .toBodilessEntity()
            .block();
    }
}

