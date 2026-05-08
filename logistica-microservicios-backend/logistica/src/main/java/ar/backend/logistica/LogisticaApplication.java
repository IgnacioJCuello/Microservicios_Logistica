package ar.backend.logistica;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;

@SpringBootApplication
public class LogisticaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogisticaApplication.class, args);
	}

	@Bean("externalWebClient")
    public WebClient externalWebClient() {
        return WebClient.create(); 
    }

    @Bean
	@LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
						.filter(addJwtTokenFilter());
    }

	private ExchangeFilterFunction addJwtTokenFilter() {
        return (request, next) -> {
            // 1. Obtenemos la autenticación del hilo actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 2. Verificamos si es un token JWT válido
            if (authentication instanceof JwtAuthenticationToken jwtToken) {
                // 3. Extraemos el string del token
                String tokenValue = jwtToken.getToken().getTokenValue();
                
                // 4. Clonamos la petición y le agregamos el header
                ClientRequest newRequest = ClientRequest.from(request)
                        .headers(headers -> headers.setBearerAuth(tokenValue))
                        .build();
                
                return next.exchange(newRequest);
            }
            return next.exchange(request);
        };
    }
}
