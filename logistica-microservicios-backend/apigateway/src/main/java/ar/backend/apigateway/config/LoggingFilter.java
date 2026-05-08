package ar.backend.apigateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class LoggingFilter {

    @Bean
    @Order(-1) // Se ejecuta antes que otros filtros
    public GlobalFilter globalLogger() {
        return (exchange, chain) -> {
            // 1. Loguear la entrada (Request)
            String path = exchange.getRequest().getPath().toString();
            String method = exchange.getRequest().getMethod().name();
            String remoteAddress = exchange.getRequest().getRemoteAddress() != null 
                                   ? exchange.getRequest().getRemoteAddress().toString() 
                                   : "Unknown";

            log.info("[GATEWAY] [ENTRADA] -> Metodo: {} | Path: {} | Origen: {}", method, path, remoteAddress);

            long startTime = System.currentTimeMillis();

            // 2. Dejar pasar la petición y loguear la salida (Response)
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                long duration = System.currentTimeMillis() - startTime;
                int statusCode = exchange.getResponse().getStatusCode() != null 
                                 ? exchange.getResponse().getStatusCode().value() 
                                 : 0;

                if (statusCode >= 400) {
                    log.warn("[GATEWAY] [SALIDA-ERROR] <- Path: {} | Status: {} | Tiempo: {}ms", path, statusCode, duration);
                } else {
                    log.info("[GATEWAY] [SALIDA-OK] <- Path: {} | Status: {} | Tiempo: {}ms", path, statusCode, duration);
                }
            }));
        };
    }
}