package ar.backend.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
public class ApigatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApigatewayApplication.class, args);
        log.info("[GATEWAY] [INICIO] El servicio ApiGateway se ha iniciado correctamente.");
        log.info("[GATEWAY] [MONITOREO] Esperando tráfico en puerto 8080...");
    }
}