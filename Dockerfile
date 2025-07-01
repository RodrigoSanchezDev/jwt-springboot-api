# ====================
# Etapa 1: COMPILACIÓN
# ====================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom.xml y src/ al contenedor
COPY pom.xml .
COPY src ./src

# Ejecutamos mvn package dentro de Docker
RUN mvn clean package -DskipTests

# ====================
# Etapa 2: RUNTIME
# ====================
# Cambiamos "eclipse-temurin:17-jre-alpine" por "eclipse-temurin:17-jre"
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el JAR que generó la etapa "build"
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Copiar application.properties al contenedor
COPY src/main/resources/application.properties /app/application.properties

# Exponemos el puerto 8080 (el que usa tu aplicación Spring Boot)
EXPOSE 8080

# Comando para ejecutar la aplicación con el archivo de configuración
ENTRYPOINT ["java","-jar","/app/app.jar","--spring.config.location=file:/app/application.properties"]
