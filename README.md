# Letras y Papeles - API REST con Spring Boot y JWT

API RESTful para la gestión de clientes, roles y operaciones de la librería "Letras y Papeles", desarrollada con Java, Spring Boot y autenticación JWT.  
Incluye autorización basada en roles y buenas prácticas de seguridad para aplicaciones modernas y microservicios.

---

## Tecnologías principales

- Java 17+
- Spring Boot
- Spring Security
- JWT (Java JWT - jjwt)
- JPA/Hibernate
- MySQL/MariaDB (soporta otros motores SQL)
- Docker (opcional)

---

## Características

- Registro y autenticación de usuarios con JWT (stateless)
- Gestión de roles y permisos (CLIENTE, EMPLEADO, GERENTE, ADMIN)
- Endpoints protegidos según rol
- Contraseñas seguras con BCrypt
- Arquitectura escalable y lista para microservicios
- Ejemplo de integración con base de datos relacional y tablas intermedias

---

## Instalación y ejecución

1. **Clona el repositorio:**
   ```bash
   git clone [https://github.com/tuusuario/letrasypapeles-jwt-springboot-api.git](https://github.com/RodrigoSanchezDev/jwt-springboot-api.git)
   cd letrasypapeles-jwt-springboot-api
   ```

2. **Configura la base de datos:**
   - Edita `src/main/resources/application.properties` con tus credenciales de MySQL/MariaDB.

3. **Compila y ejecuta:**
   ```bash
   ./mvnw spring-boot:run
   ```

   O usa Docker:
   ```bash
   docker-compose up --build
   ```

---

## Endpoints principales

- `POST /api/auth/register` — Registro de usuario con rol
- `POST /api/auth/login` — Autenticación y obtención de JWT
- `GET /api/clientes` — Listado de clientes (protegido)
- `GET /api/test/empleado` — Endpoint protegido para rol EMPLEADO

---

## Seguridad

- Autenticación y autorización con JWT
- Roles y permisos gestionados en base de datos
- Contraseñas hasheadas con BCrypt
- Filtros de seguridad personalizados con Spring Security

---

## Autor

Desarrollado por **Rodrigo Sanchez**  
[Sanchezdev.com](https://sanchezdev.com)

---

## Licencia

MIT

---

> Proyecto académico y de referencia para arquitecturas seguras con Spring Boot y JWT.
