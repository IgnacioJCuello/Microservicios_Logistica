# Proyecto: Backend Microservicios (Java + Spring Cloud)

## 🧠 Arquitectura de Microservicios

Sistema backend basado en microservicios desarrollado con Spring Boot, implementando patrones de arquitectura distribuida con descubrimiento de servicios y API Gateway.

### 🚀 Descripción

Este proyecto implementa una arquitectura de microservicios donde distintos servicios independientes se comunican entre sí mediante un servidor de descubrimiento (Eureka) y un API Gateway que centraliza las peticiones.

La solución está containerizada utilizando Docker, permitiendo su despliegue de forma sencilla y escalable.

### 🛠️ Tecnologías utilizadas
 - Java
 - Spring Boot
 - Spring Cloud Netflix Eureka
 - API Gateway
 - Docker / Docker Compose
 - Maven
### ⚙️ Componentes del sistema
 - Eureka Server → descubrimiento de servicios
 - API Gateway → punto de entrada único
 - Microservicios → lógica distribuida (ej: flota, etc.)
 - Docker Compose → orquestación

### 📂 Estructura
 - /eureka-server → servidor de descubrimiento
 - /apigateway → gateway de entrada
 - /flota → servicios independientes
 - /logística → servicios independientes
 - docker-compose.yml → orquestación

### 📌 Contexto

**Trabajo práctico académico orientado al diseño de arquitecturas backend basadas en microservicios.**

### 📌 Dependencias externas

Este proyecto utiliza un componente externo para la gestión de mapas y coordenadas que no se encuentra incluido en el repositorio debido a su tamaño (~3GB).
