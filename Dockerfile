# Stage 1: Compilar todo el proyecto unificado
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar el POM padre y la estructura de todos los submódulos
COPY pom.xml ./
COPY ApiGateway/pom.xml ./ApiGateway/
COPY BFF/pom.xml ./BFF/
COPY Patient-Portal/pom.xml ./Patient-Portal/
COPY waitlist-service/pom.xml ./waitlist-service/
COPY AutoReasign-Service/pom.xml ./AutoReasign-Service/

# Copiar el código fuente de absolutamente todos los servicios
COPY ApiGateway/src ./ApiGateway/src
COPY BFF/src ./BFF/src
COPY Patient-Portal/src ./Patient-Portal/src
COPY waitlist-service/src ./waitlist-service/src
COPY AutoReasign-Service/src ./AutoReasign-Service/src

# Compilar todo el ecosistema de una sola vez respetando dependencias mutuas
RUN mvn clean package -DskipTests

# Stage 2: Base para las imágenes de ejecución individuales
FROM eclipse-temurin:17-jre-alpine AS runner
WORKDIR /app
# Esta etapa queda lista para que docker-compose extraiga el .jar que corresponda