# ⚙️ Red Norte - Backend

Backend desarrollado con **Spring Boot** bajo una arquitectura de **microservicios** para el sistema **Red Norte**. Este proyecto centraliza la lógica de negocio de la plataforma, exponiendo una serie de servicios REST encargados de la autenticación, administración de pacientes, gestión de citas médicas y comunicación con la base de datos.

La solución está compuesta por múltiples servicios independientes coordinados mediante un **API Gateway**, permitiendo una mayor escalabilidad, modularidad y facilidad de mantenimiento.

---

## Características

Entre las funcionalidades implementadas en el backend se encuentran:

- Autenticación y autorización mediante JWT.
- API Gateway para centralizar el acceso a los microservicios.
- Backend For Frontend (BFF) para el consumo desde la aplicación web.
- Administración de pacientes.
- Gestión de listas de espera.
- Reasignación automática de citas.
- Persistencia de información utilizando MySQL.
- Exposición de servicios REST.
- Inicialización automática de la base de datos mediante Docker.

---

## Tecnologías utilizadas

| Tecnología | Uso |
|------------|-----|
| Java 21 | Desarrollo de los microservicios |
| Spring Boot | Framework principal |
| Spring Cloud Gateway | Enrutamiento de solicitudes |
| Spring Security | Seguridad de la aplicación |
| JWT | Autenticación mediante tokens |
| Spring Data JPA | Acceso a datos |
| Hibernate | Persistencia ORM |
| MySQL | Base de datos relacional |
| Docker | Contenedores |
| Docker Compose | Orquestación de servicios |
| Maven | Gestión de dependencias |

---

## Arquitectura

El backend está construido siguiendo una arquitectura basada en **microservicios**, donde cada módulo desarrolla una responsabilidad específica dentro del sistema.

Los componentes se encuentran desacoplados entre sí, facilitando el mantenimiento del código, la incorporación de nuevas funcionalidades y el despliegue independiente de los servicios.

Las solicitudes externas ingresan a través del **API Gateway**, el cual redirige las peticiones hacia el microservicio correspondiente. La autenticación y autorización son administradas mediante **Spring Security** utilizando **JSON Web Tokens (JWT)**.

---

## Organización del proyecto

```text
REDNORTEBACK/
│
├── ApiGateway/
│   └── Gateway encargado del enrutamiento de solicitudes.
│
├── AutoReasign-Service/
│   └── Manejo de citas que incluye la reasignación de estas por cancelación.
│
├── BFF/
│   └── Backend For Frontend utilizado por la aplicación web.
│
├── Patient-Portal/
│   └── Gestión de pacientes e información clínica.
│
├── security/
│   └── Configuración de autenticación y autorización.
│
├── waitlist-service/
│   └── Administración de listas de espera.
│
├── init-db/
│   └── Scripts de inicialización de MySQL.
│
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

---

## Comunicación entre servicios

La aplicación está compuesta por varios microservicios que interactúan mediante llamadas REST.

Cada módulo concentra una responsabilidad específica, permitiendo mantener una arquitectura desacoplada y facilitando la evolución del sistema sin afectar el funcionamiento de los demás componentes.

El acceso desde el frontend se realiza a través del **Backend For Frontend (BFF)**, el cual consume los distintos servicios internos según la operación solicitada.

---

## Instalación

Clonar el repositorio

```bash
git clone https://github.com/doomedplayer/rednorteBack.git
```

Ingresar al proyecto

```bash
cd rednorteBack
```

Compilar todos los módulos del proyecto

```bash
mvn clean package -DskipTests
```

---

## Ejecución

Detener contenedores existentes (opcional)

```bash
docker compose down
```

Levantar todos los servicios

```bash
docker compose up --build
```

Docker Compose iniciará automáticamente:

- API Gateway.
- Backend For Frontend.
- Servicios de negocio.
- Servicio de seguridad.
- Base de datos MySQL.

Los servicios quedarán disponibles utilizando los puertos configurados entre:

```text
8080 - 8085
```

---

## Seguridad

La autenticación del sistema se implementa mediante **Spring Security** utilizando **JWT (JSON Web Token)**.

Una vez autenticado el usuario, se genera un token que debe acompañar las solicitudes a los endpoints protegidos. Cada petición es validada antes de permitir el acceso a los recursos disponibles en los distintos microservicios.

---

## Base de datos

La persistencia de la información se realiza mediante **MySQL**, utilizando **Spring Data JPA** junto con **Hibernate** para la administración de las entidades y el acceso a los datos.

La creación e inicialización de la base de datos se ejecuta automáticamente desde el directorio **init-db** al levantar el proyecto mediante Docker Compose.

---

## Scripts disponibles

| Comando | Descripción |
|---------|-------------|
| mvn clean package -DskipTests | Compila todos los módulos del proyecto. |
| docker compose up --build | Construye e inicia todos los servicios y la base de datos. |
| docker compose down | Detiene y elimina los contenedores creados. |
