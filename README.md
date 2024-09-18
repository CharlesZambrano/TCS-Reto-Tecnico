
# Reto Técnico Backend: Microservicios con Spring Boot, PostgreSQL y RabbitMQ

Este proyecto implementa un sistema basado en microservicios utilizando **Spring Boot**, **PostgreSQL**, **RabbitMQ** y **Docker Compose**. El sistema maneja la información de clientes, cuentas y movimientos, aplicando principios de diseño **SOLID** y buenas prácticas de arquitectura de software.

## Tabla de Contenidos
1. [Descripción General](#descripción-general)
2. [Arquitectura](#arquitectura)
3. [Requisitos](#requisitos)
4. [Instalación y Despliegue](#instalación-y-despliegue)
5. [Microservicios](#microservicios)
    - [Clientes y Personas](#clientes-y-personas)
    - [Cuentas y Movimientos](#cuentas-y-movimientos)
6. [Bases de Datos](#bases-de-datos)
7. [Mensajería con RabbitMQ](#mensajería-con-rabbitmq)
8. [Pruebas](#pruebas)
9. [Documentación de la API](#documentación-de-la-api)
10. [Principios SOLID Aplicados](#principios-solid-aplicados)
11. [Contribución](#contribución)

## Descripción General

Este proyecto tiene como objetivo demostrar la implementación de microservicios que manejan entidades de **Cliente**, **Cuenta** y **Movimiento**, con operaciones **CRUD** y funcionalidades como validación de saldo, registro de movimientos y generación de reportes. La solución se despliega en contenedores Docker y utiliza RabbitMQ para la comunicación asincrónica entre microservicios.

## Arquitectura

La aplicación está dividida en dos microservicios:

1. **Clientes y Personas**: Gestiona la información de personas y clientes.
2. **Cuentas y Movimientos**: Gestiona las cuentas bancarias y los movimientos financieros.

Ambos microservicios interactúan con bases de datos **PostgreSQL** separadas y se comunican de forma asincrónica mediante **RabbitMQ**.

## Requisitos

Para ejecutar este proyecto, necesitas tener instalados los siguientes componentes:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 21](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
- [Postman](https://www.postman.com/) (Opcional, para pruebas manuales)

## Instalación y Despliegue

### 1. Clonar el repositorio

Clona el repositorio en tu máquina local:

```bash
git clone https://github.com/usuario/repo.git
cd repo
```

### 2. Compilar los microservicios

Antes de ejecutar `docker-compose`, necesitas compilar los microservicios para generar los archivos JAR necesarios. Navega a cada directorio y ejecuta el siguiente comando:

```bash
# Para el microservicio clientes_personas
cd clientes_personas
./gradlew clean build

# Para el microservicio cuentas_movimientos
cd ../cuentas_movimientos
./gradlew clean build

# Vuelve a la raíz del proyecto
cd ..
```

### 3. Levantar los servicios con Docker Compose

Una vez que los microservicios estén compilados, ejecuta el siguiente comando para levantar los contenedores de Docker:

```bash
docker-compose up --build
```

Esto construirá las imágenes de los microservicios y levantará los servicios necesarios, como **PostgreSQL** y **RabbitMQ**.

### 4. Acceder a los servicios

- **Clientes y Personas**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Cuentas y Movimientos**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

Puedes acceder al panel de administración de RabbitMQ en [http://localhost:15672](http://localhost:15672) (usuario: `guest`, contraseña: `guest`).

## Microservicios

### Clientes y Personas

- **Base de Datos**: `clientes_db`
- **Endpoints**:
  - `POST /clientes`: Crear un nuevo cliente.
  - `GET /clientes/{id}`: Obtener la información de un cliente por ID.
  - `PUT /clientes/{id}`: Actualizar la información de un cliente.
  - `DELETE /clientes/{id}`: Eliminar un cliente.
- **Entidades**:
  - **Persona**: nombre, género, edad, identificación, dirección, teléfono.
  - **Cliente**: extiende Persona, con `cliente_id`, contraseña y estado.

### Cuentas y Movimientos

- **Base de Datos**: `cuentas_db`
- **Endpoints**:
  - `POST /cuentas`: Crear una nueva cuenta.
  - `POST /movimientos`: Registrar un movimiento (depósito/retiro).
  - `GET /movimientos`: Obtener historial de movimientos.
- **Entidades**:
  - **Cuenta**: número de cuenta, tipo, saldo inicial, estado.
  - **Movimiento**: fecha, tipo, valor, saldo resultante.

## Bases de Datos

El proyecto utiliza dos bases de datos **PostgreSQL** desplegadas mediante Docker. Las bases de datos se configuran a través del archivo `docker-compose.yml`:

```yaml
services:
  postgres_clientes:
    image: postgres:14
    environment:
      POSTGRES_DB: clientes_db
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin
    ports:
      - "5432:5432"

  postgres_cuentas:
    image: postgres:14
    environment:
      POSTGRES_DB: cuentas_db
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin
    ports:
      - "5433:5432"
```

## Mensajería con RabbitMQ

Se utiliza **RabbitMQ** como broker de mensajes para la comunicación asincrónica entre los microservicios. Las colas y los exchanges son configurados automáticamente por los microservicios en el arranque.

## Pruebas

El proyecto incluye pruebas unitarias y de integración:

1. **Pruebas Unitarias**: Implementadas con **JUnit** y **Mockito** para validar la lógica de negocio.
2. **Pruebas de Integración**: Aseguran la correcta comunicación entre microservicios y la interacción con RabbitMQ.

Para ejecutar las pruebas:
```bash
./mvnw test
```

## Documentación de la API

La documentación de la API está disponible a través de **Swagger** en los siguientes endpoints:

- **Clientes y Personas**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Cuentas y Movimientos**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

## Principios SOLID Aplicados

- **Responsabilidad Única (SRP)**: Cada clase tiene una única responsabilidad clara.
- **Abierto/Cerrado (OCP)**: El sistema permite la extensión sin modificar el código existente.
- **Liskov (LSP)**: Herencias como Cliente -> Persona se implementan respetando las propiedades.
- **Segregación de Interfaces (ISP)**: Los microservicios están bien segregados.
- **Inversión de Dependencias (DIP)**: Inyección de dependencias para desacoplar servicios.

## Contribución

Si deseas contribuir a este proyecto, sigue los siguientes pasos:

1. Haz un fork del repositorio.
2. Crea una rama con una nueva funcionalidad (`git checkout -b feature/nueva-funcionalidad`).
3. Haz commit de tus cambios (`git commit -m 'Añade nueva funcionalidad'`).
4. Haz push a la rama (`git push origin feature/nueva-funcionalidad`).
5. Abre un Pull Request.

---

¡Gracias por tu interés en este proyecto! Si tienes preguntas, no dudes en abrir un issue.
