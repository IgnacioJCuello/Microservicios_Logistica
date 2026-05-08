package ar.backend.logistica.client;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value; // <--- IMPORTANTE: Agregar este import
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ar.backend.logistica.dto.OsrmResponse;

@Component
public class OsrmClient {

    private final WebClient webClient;


    // La sintaxis es: "${nombre.propiedad:valor_por_defecto}"
    @Value("${osrm.url:http://localhost:5000}")
    private String osrmUrlBase;

    public OsrmClient(@Qualifier("externalWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public OsrmResponse route(double latOrigen, double lonOrigen,
                              double latDestino, double lonDestino) {

        // CAMBIO 2: Usamos la variable en lugar del texto fijo
        String url = String.format(
            Locale.US,
            "%s/route/v1/driving/%f,%f;%f,%f", 
            this.osrmUrlBase,
            lonOrigen, latOrigen, lonDestino, latDestino
        );

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(OsrmResponse.class)
                .block(); 
    }
}