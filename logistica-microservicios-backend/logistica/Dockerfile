# 1. Usamos una imagen base ligera con Java 21 (Alpine Linux)
FROM eclipse-temurin:21-jdk-alpine

# 2. (Opcional) Creamos un volumen temporal para Tomcat
VOLUME /tmp

# 3. Definimos una variable para encontrar el archivo .jar compilado
ARG JAR_FILE=target/*.jar

# 4. Copiamos ese .jar desde tu carpeta local hacia adentro del contenedor con el nombre app.jar
COPY ${JAR_FILE} app.jar

# 5. El comando que se ejecuta al iniciar el contenedor
ENTRYPOINT ["java","-jar","/app.jar"]