package ar.backend.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            // 1. Deshabilitar CSRF para APIs REST stateless
            .csrf(csrf -> csrf.disable())

            // 2. Reglas de Autorización
            .authorizeExchange(exchange -> exchange
                // Permitir documentación (Swagger) sin login
                .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                
                // Permitir Actuator (Health checks) para ver si el servicio está vivo
                .pathMatchers("/actuator/**").permitAll()

                // BLOQUEO TOTAL: Todo lo demás exige Token válido
                .anyExchange().authenticated()
            )

            // 3. Validación de Token JWT (Resource Server)
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}