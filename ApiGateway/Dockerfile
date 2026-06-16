# Etapa 1: Compilación de la aplicación usando Maven
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar el POM padre desde la raíz
COPY pom.xml ./ 

# Copiar los submódulos necesarios para la compilación de la arquitectura
COPY ApiGateway/pom.xml ./ApiGateway/
COPY BFF/pom.xml ./BFF/
COPY Patient-Portal/pom.xml ./Patient-Portal/
COPY waitlist-service/pom.xml ./waitlist-service/
COPY AutoReasign-Service/pom.xml ./AutoReasign-Service/

# Copiar el código fuente específico de ESTE microservicio argumentado por Docker Compose
ARG SERVICE_DIR
COPY ${SERVICE_DIR}/src ./${SERVICE_DIR}/src

# Compilar enfocándose únicamente en el módulo correspondiente
RUN mvn clean package -pl ${SERVICE_DIR} -am -DskipTests

# Etapa 2: Imagen ligera de ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ARG SERVICE_DIR
COPY --from=build /app/${SERVICE_DIR}/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]