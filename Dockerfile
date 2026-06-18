# Stage 1: Compilar todo el proyecto unificado con JAVA 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
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

# 1. Instalar el proyecto padre primero (para que los módulos hereden propiedades)
RUN mvn clean install -N -DskipTests

# 2. Instalar primero los servicios de los que depende el BFF 
# (-pl compila proyectos específicos, -am compila sus dependencias si las tienen)
RUN mvn clean install -pl Patient-Portal,waitlist-service -am -DskipTests

# 3. Finalmente, compilar y empaquetar todo el ecosistema (incluyendo el BFF y ApiGateway)
RUN mvn clean package -DskipTests

# Stage 2: Base para las imágenes de ejecución con JAVA 21
FROM eclipse-temurin:21-jre-alpine AS runner
WORKDIR /app

# Copiar los .jar compilados desde la etapa 1 (build) a esta etapa final
COPY --from=build /app/ApiGateway/target ./ApiGateway/target
COPY --from=build /app/BFF/target ./BFF/target
COPY --from=build /app/Patient-Portal/target ./Patient-Portal/target
COPY --from=build /app/waitlist-service/target ./waitlist-service/target
COPY --from=build /app/AutoReasign-Service/target ./AutoReasign-Service/target