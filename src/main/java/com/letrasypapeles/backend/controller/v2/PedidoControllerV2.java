package com.letrasypapeles.backend.controller.v2;

import com.letrasypapeles.backend.assembler.PedidoRepresentationModelAssembler;
import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.service.PedidoService;
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
 * Controlador REST V2 para gestión de pedidos con soporte HATEOAS
 * Proporciona navegación hipermedia y enlaces dinámicos
 * 
 * @author Rodrigo Sanchez
 * @version 2.0.0
 */
@Tag(name = "Pedidos V2 (HATEOAS)", description = "API V2 - Gestión de pedidos con navegación hipermedia y enlaces dinámicos")
@RestController
@RequestMapping("/api/v2/pedidos")
@SecurityRequirement(name = "bearerAuth")
public class PedidoControllerV2 {

    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private PedidoRepresentationModelAssembler pedidoAssembler;

    @Operation(
        summary = "Obtener todos los pedidos (HATEOAS)", 
        description = "Retorna una colección de pedidos con enlaces hipermedia para navegación y acciones disponibles. " +
                     "Incluye enlaces self, navegación a recursos relacionados y acciones CRUD."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Colección de pedidos obtenida exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/CollectionModel"))
        )
    })
    @GetMapping
    public CollectionModel<EntityModel<Pedido>> obtenerTodos() {
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerTodos().stream()
                .map(pedidoAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(pedidos)
                .add(linkTo(methodOn(PedidoControllerV2.class).obtenerTodos()).withSelfRel())
                .add(linkTo(methodOn(PedidoControllerV2.class).crearPedido(null)).withRel("create"))
                .add(linkTo(methodOn(PedidoControllerV2.class).obtenerTodos()).withRel("refresh"));
    }

    @Operation(
        summary = "Obtener pedido por ID (HATEOAS)", 
        description = "Retorna un pedido específico con enlaces hipermedia a recursos relacionados y acciones disponibles"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Pedido encontrado con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> obtenerPorId(
            @Parameter(description = "ID del pedido", example = "1")
            @PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(pedido -> ResponseEntity.ok(pedidoAssembler.toModel(pedido)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo pedido (HATEOAS)", 
        description = "Crea un nuevo pedido y retorna la entidad con enlaces hipermedia"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Pedido creado exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Pedido>> crearPedido(@RequestBody Pedido pedido) {
        Pedido nuevoPedido = pedidoService.guardar(pedido);
        
        EntityModel<Pedido> pedidoModel = pedidoAssembler.toModel(nuevoPedido);
        
        return ResponseEntity
                .created(URI.create(pedidoModel.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                .body(pedidoModel);
    }

    @Operation(
        summary = "Actualizar pedido (HATEOAS)", 
        description = "Actualiza un pedido existente y retorna la entidad con enlaces hipermedia actualizados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Pedido actualizado exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pedido>> actualizarPedido(
            @Parameter(description = "ID del pedido", example = "1")
            @PathVariable Long id, 
            @RequestBody Pedido pedido) {
        return pedidoService.obtenerPorId(id)
                .map(pedidoExistente -> {
                    pedido.setId(id);
                    Pedido pedidoActualizado = pedidoService.guardar(pedido);
                    return ResponseEntity.ok(pedidoAssembler.toModel(pedidoActualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Eliminar pedido (HATEOAS)", 
        description = "Elimina un pedido y retorna enlaces para navegar de vuelta a la colección"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(
            @Parameter(description = "ID del pedido", example = "1")
            @PathVariable Long id) {
        return pedidoService.obtenerPorId(id)
                .map(pedido -> {
                    pedidoService.eliminar(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Obtener pedidos por cliente (HATEOAS)",
        description = "Retorna todos los pedidos de un cliente específico con enlaces hipermedia"
    )
    @GetMapping("/cliente/{clienteId}")
    public CollectionModel<EntityModel<Pedido>> obtenerPorCliente(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long clienteId) {
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerPorClienteId(clienteId).stream()
                .map(pedidoAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(pedidos)
                .add(linkTo(methodOn(PedidoControllerV2.class).obtenerPorCliente(clienteId)).withSelfRel())
                .add(linkTo(methodOn(PedidoControllerV2.class).obtenerTodos()).withRel("todos-pedidos"))
                .add(linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(clienteId)).withRel("cliente"));
    }

    @Operation(
        summary = "Obtener pedidos por estado (HATEOAS)",
        description = "Retorna pedidos filtrados por estado con enlaces hipermedia"
    )
    @GetMapping("/estado/{estado}")
    public CollectionModel<EntityModel<Pedido>> obtenerPorEstado(
            @Parameter(description = "Estado del pedido", example = "PENDIENTE")
            @PathVariable String estado) {
        List<EntityModel<Pedido>> pedidos = pedidoService.obtenerPorEstado(estado).stream()
                .map(pedidoAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(pedidos)
                .add(linkTo(methodOn(PedidoControllerV2.class).obtenerPorEstado(estado)).withSelfRel())
                .add(linkTo(methodOn(PedidoControllerV2.class).obtenerTodos()).withRel("todos-pedidos"));
    }
}
