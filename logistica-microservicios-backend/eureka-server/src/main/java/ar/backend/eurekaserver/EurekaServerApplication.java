package ar.backend.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
		
		log.info("[EUREKA_SERVER] [INICIO] El servidor de registro se ha iniciado correctamente.");
		log.info("[EUREKA_SERVER] [MONITOREO] Dashboard disponible en http://localhost:8761");
	}

}