package com.letrasypapeles.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API con soporte HATEOAS
 * 
 * @author Rodrigo Sanchez
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .openapi("3.0.0")  // Especificar versión OpenAPI explícitamente
                .info(new Info()
                        .title("Letras y Papeles - API REST HATEOAS")
                        .description("API REST para gestión de librería con autenticación JWT y navegación hipermedia HATEOAS. " +
                                "Sistema completo de gestión de inventario, clientes, productos, pedidos y reservas con enlaces hipermedia.")
                        .version("v2.0.0")
                        .contact(new Contact()
                                .name("Rodrigo Sanchez")
                                .email("Rodrigo@sanchezdev.com")
                                .url("https://sanchezdev.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese el token JWT en el formato: Bearer {token}"))
                        // Esquemas HATEOAS para documentación
                        .addSchemas("HateoasLink", new ObjectSchema()
                                .addProperty("href", new StringSchema().description("URL del enlace"))
                                .addProperty("rel", new StringSchema().description("Relación del enlace"))
                                .addProperty("type", new StringSchema().description("Tipo MIME del enlace")))
                        .addSchemas("HateoasLinks", new ObjectSchema()
                                .addProperty("self", new Schema<>().$ref("#/components/schemas/HateoasLink"))
                                .addProperty("edit", new Schema<>().$ref("#/components/schemas/HateoasLink"))
                                .addProperty("delete", new Schema<>().$ref("#/components/schemas/HateoasLink")))
                        .addSchemas("EntityModel", new ObjectSchema()
                                .addProperty("_links", new Schema<>().$ref("#/components/schemas/HateoasLinks"))
                                .description("Modelo de entidad con enlaces HATEOAS"))
                        .addSchemas("CollectionModel", new ObjectSchema()
                                .addProperty("_embedded", new ObjectSchema()
                                        .description("Contenido embebido de la colección"))
                                .addProperty("_links", new Schema<>().$ref("#/components/schemas/HateoasLinks"))
                                .description("Modelo de colección con enlaces HATEOAS")));
    }
}
