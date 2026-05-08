package ar.backend.flota.client;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ar.backend.flota.dto.TramosAsignadosResponseDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TramoClient {

    private final WebClient.Builder webClientBuilder;

    public TramosAsignadosResponseDTO obtenerTramosPorIds(List<Integer> idsTramos) {
        return webClientBuilder.build()
                .post()
                .uri("http://logistica/tramos/asignados")
                .bodyValue(idsTramos)
                .retrieve()
                .bodyToMono(TramosAsignadosResponseDTO.class)
                .block();
    }
}

