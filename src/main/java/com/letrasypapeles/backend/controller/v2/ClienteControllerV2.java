package com.letrasypapeles.backend.controller.v2;

import com.letrasypapeles.backend.assembler.ClienteRepresentationModelAssembler;
import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador REST V2 para gestión de clientes con soporte HATEOAS
 * Proporciona navegación hipermedia y enlaces dinámicos
 * 
 * @author Rodrigo Sanchez
 * @version 2.0.0
 */
@Tag(name = "Clientes V2 (HATEOAS)", description = "API V2 - Gestión de clientes con navegación hipermedia y enlaces dinámicos")
@RestController
@RequestMapping("/api/v2/clientes")
@SecurityRequirement(name = "bearerAuth")
public class ClienteControllerV2 {

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ClienteRepresentationModelAssembler clienteAssembler;

    @Operation(
        summary = "Obtener todos los clientes (HATEOAS)", 
        description = "Retorna una colección de clientes con enlaces hipermedia para navegación y acciones disponibles. " +
                     "Incluye enlaces self, navegación a recursos relacionados y acciones CRUD."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Colección de clientes obtenida exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/CollectionModel"))
        )
    })
    @GetMapping
    public CollectionModel<EntityModel<Cliente>> obtenerTodos() {
        List<EntityModel<Cliente>> clientes = clienteService.obtenerTodos().stream()
                .peek(cliente -> cliente.setContraseña(null)) // Ocultar contraseña
                .map(clienteAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(clientes)
                .add(linkTo(methodOn(ClienteControllerV2.class).obtenerTodos()).withSelfRel())
                .add(linkTo(methodOn(ClienteControllerV2.class).crearCliente(null)).withRel("create"))
                .add(linkTo(methodOn(ClienteControllerV2.class).obtenerTodos()).withRel("refresh"));
    }

    @Operation(
        summary = "Obtener cliente por ID (HATEOAS)", 
        description = "Retorna un cliente específico con enlaces hipermedia a recursos relacionados y acciones disponibles"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Cliente encontrado con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> obtenerPorId(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(cliente -> {
                    cliente.setContraseña(null); // Ocultar contraseña
                    return ResponseEntity.ok(clienteAssembler.toModel(cliente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo cliente (HATEOAS)", 
        description = "Crea un nuevo cliente y retorna la entidad con enlaces hipermedia"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Cliente creado exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Cliente>> crearCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.registrarCliente(cliente);
        nuevoCliente.setContraseña(null); // Ocultar contraseña
        
        EntityModel<Cliente> clienteModel = clienteAssembler.toModel(nuevoCliente);
        
        return ResponseEntity
                .created(URI.create(clienteModel.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                .body(clienteModel);
    }

    @Operation(
        summary = "Actualizar cliente (HATEOAS)", 
        description = "Actualiza un cliente existente y retorna la entidad con enlaces hipermedia actualizados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Cliente actualizado exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Cliente>> actualizarCliente(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id, 
            @RequestBody Cliente cliente) {
        return clienteService.obtenerPorId(id)
                .map(clienteExistente -> {
                    cliente.setId(id);
                    Cliente clienteActualizado = clienteService.actualizarCliente(cliente);
                    clienteActualizado.setContraseña(null); // Ocultar contraseña
                    return ResponseEntity.ok(clienteAssembler.toModel(clienteActualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Eliminar cliente (HATEOAS)", 
        description = "Elimina un cliente y retorna enlaces para navegar de vuelta a la colección"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(cliente -> {
                    clienteService.eliminar(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Endpoint raíz de la API V2 (HATEOAS)",
        description = "Punto de entrada para descubrir todos los recursos disponibles en la API V2"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Enlaces de navegación principal de la API"
    )
    @GetMapping("/")
    public EntityModel<String> apiRoot() {
        return EntityModel.of("API V2 - Letras y Papeles (HATEOAS)")
                .add(linkTo(methodOn(ClienteControllerV2.class).obtenerTodos()).withRel("clientes"))
                .add(linkTo(methodOn(ClienteControllerV2.class).crearCliente(null)).withRel("crear-cliente"))
                .add(linkTo(methodOn(ClienteControllerV2.class).apiRoot()).withSelfRel());
    }
}
