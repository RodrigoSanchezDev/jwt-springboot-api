package com.letrasypapeles.backend.controller.v2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador raíz para la API V2 con HATEOAS
 * Proporciona el punto de entrada principal para descubrir todos los recursos disponibles
 * 
 * @author Rodrigo Sanchez
 * @version 2.0.0
 */
@Tag(name = "API V2 Root (HATEOAS)", description = "Punto de entrada principal para la API V2 con navegación hipermedia")
@RestController
@RequestMapping("/api/v2")
@SecurityRequirement(name = "bearerAuth")
public class ApiRootControllerV2 {

    @Operation(
        summary = "Punto de entrada de la API V2 (HATEOAS)",
        description = "Endpoint principal que proporciona enlaces de descubrimiento para todos los recursos disponibles en la API V2. " +
                     "Implementa el principio HATEOAS de discoverability, permitiendo a los clientes navegar por la API sin conocimiento previo de las URLs."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Enlaces de navegación principal de la API V2",
        content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
    )
    @GetMapping
    public EntityModel<ApiInfo> apiRoot() {
        ApiInfo apiInfo = new ApiInfo(
            "Letras y Papeles API V2",
            "2.0.0",
            "API REST con soporte HATEOAS para gestión completa de librería",
            "Navegación hipermedia habilitada"
        );

        return EntityModel.of(apiInfo)
                // Enlaces principales a recursos
                .add(linkTo(methodOn(ClienteControllerV2.class).obtenerTodos()).withRel("clientes"))
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("productos"))
                .add(linkTo(methodOn(PedidoControllerV2.class).obtenerTodos()).withRel("pedidos"))
                .add(linkTo(methodOn(ReservaControllerV2.class).obtenerTodas()).withRel("reservas"))
                // Enlaces de creación
                .add(linkTo(methodOn(ClienteControllerV2.class).crearCliente(null)).withRel("crear-cliente"))
                .add(linkTo(methodOn(ProductoControllerV2.class).crearProducto(null)).withRel("crear-producto"))
                .add(linkTo(methodOn(PedidoControllerV2.class).crearPedido(null)).withRel("crear-pedido"))
                .add(linkTo(methodOn(ReservaControllerV2.class).crearReserva(null)).withRel("crear-reserva"))
                // Enlaces de navegación
                .add(linkTo(methodOn(ApiRootControllerV2.class).apiRoot()).withSelfRel())
                .add(linkTo(methodOn(ApiRootControllerV2.class).apiDocumentation()).withRel("documentation"))
                // Enlaces informativos
                .add(linkTo(methodOn(ApiRootControllerV2.class).apiInfo()).withRel("info"));
    }

    @Operation(
        summary = "Información de la API V2",
        description = "Retorna información detallada sobre la API V2 y sus capacidades HATEOAS"
    )
    @GetMapping("/info")
    public EntityModel<ApiInfo> apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
            "Letras y Papeles API V2",
            "2.0.0",
            "API REST con soporte HATEOAS completo para gestión de librería. " +
            "Incluye navegación hipermedia, descubrimiento automático de recursos y enlaces dinámicos.",
            "HATEOAS (Hypermedia as the Engine of Application State) habilitado"
        );

        return EntityModel.of(apiInfo)
                .add(linkTo(methodOn(ApiRootControllerV2.class).apiInfo()).withSelfRel())
                .add(linkTo(methodOn(ApiRootControllerV2.class).apiRoot()).withRel("root"));
    }

    @Operation(
        summary = "Documentación de la API V2",
        description = "Enlaces a la documentación y recursos de ayuda para la API V2"
    )
    @GetMapping("/docs")
    public EntityModel<String> apiDocumentation() {
        return EntityModel.of("Documentación API V2 - HATEOAS")
                .add(linkTo(methodOn(ApiRootControllerV2.class).apiDocumentation()).withSelfRel())
                .add(linkTo(methodOn(ApiRootControllerV2.class).apiRoot()).withRel("root"))
                // Enlaces externos a documentación
                .add(linkTo("http://localhost:8080/swagger-ui/index.html").withRel("swagger-ui"))
                .add(linkTo("http://localhost:8080/v3/api-docs").withRel("openapi-spec"));
    }

    /**
     * Clase interna para información de la API
     */
    public static class ApiInfo {
        private String name;
        private String version;
        private String description;
        private String features;

        public ApiInfo(String name, String version, String description, String features) {
            this.name = name;
            this.version = version;
            this.description = description;
            this.features = features;
        }

        // Getters
        public String getName() { return name; }
        public String getVersion() { return version; }
        public String getDescription() { return description; }
        public String getFeatures() { return features; }

        // Setters
        public void setName(String name) { this.name = name; }
        public void setVersion(String version) { this.version = version; }
        public void setDescription(String description) { this.description = description; }
        public void setFeatures(String features) { this.features = features; }
    }
}
