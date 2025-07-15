package com.letrasypapeles.backend.controller.v2;

import com.letrasypapeles.backend.assembler.ProductoRepresentationModelAssembler;
import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.service.ProductoService;
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
 * Controlador REST V2 para gestión de productos con soporte HATEOAS
 * Proporciona navegación hipermedia y enlaces dinámicos
 * 
 * @author Rodrigo Sanchez
 * @version 2.0.0
 */
@Tag(name = "Productos V2 (HATEOAS)", description = "API V2 - Gestión de productos con navegación hipermedia y enlaces dinámicos")
@RestController
@RequestMapping("/api/v2/productos")
@SecurityRequirement(name = "bearerAuth")
public class ProductoControllerV2 {

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private ProductoRepresentationModelAssembler productoAssembler;

    @Operation(
        summary = "Obtener todos los productos (HATEOAS)", 
        description = "Retorna una colección de productos con enlaces hipermedia para navegación y acciones disponibles. " +
                     "Incluye enlaces self, navegación a recursos relacionados y acciones CRUD."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Colección de productos obtenida exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/CollectionModel"))
        )
    })
    @GetMapping
    public CollectionModel<EntityModel<Producto>> obtenerTodos() {
        List<EntityModel<Producto>> productos = productoService.obtenerTodos().stream()
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos)
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).crearProducto(null)).withRel("create"))
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("refresh"));
    }

    @Operation(
        summary = "Obtener producto por ID (HATEOAS)", 
        description = "Retorna un producto específico con enlaces hipermedia a recursos relacionados y acciones disponibles"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Producto encontrado con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerPorId(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(producto -> ResponseEntity.ok(productoAssembler.toModel(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo producto (HATEOAS)", 
        description = "Crea un nuevo producto y retorna la entidad con enlaces hipermedia"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Producto creado exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Producto>> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        
        EntityModel<Producto> productoModel = productoAssembler.toModel(nuevoProducto);
        
        return ResponseEntity
                .created(URI.create(productoModel.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                .body(productoModel);
    }

    @Operation(
        summary = "Actualizar producto (HATEOAS)", 
        description = "Actualiza un producto existente y retorna la entidad con enlaces hipermedia actualizados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Producto actualizado exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id, 
            @RequestBody Producto producto) {
        return productoService.obtenerPorId(id)
                .map(productoExistente -> {
                    producto.setId(id);
                    Producto productoActualizado = productoService.guardar(producto);
                    return ResponseEntity.ok(productoAssembler.toModel(productoActualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Eliminar producto (HATEOAS)", 
        description = "Elimina un producto y retorna enlaces para navegar de vuelta a la colección"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(producto -> {
                    productoService.eliminar(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Buscar productos por nombre (HATEOAS)",
        description = "Retorna productos que contienen el texto especificado en su nombre con enlaces hipermedia"
    )
    @GetMapping("/buscar")
    public CollectionModel<EntityModel<Producto>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre", example = "libro")
            @RequestParam String nombre) {
        List<EntityModel<Producto>> productos = productoService.obtenerTodos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .map(productoAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(productos)
                .add(linkTo(methodOn(ProductoControllerV2.class).buscarPorNombre(nombre)).withSelfRel())
                .add(linkTo(methodOn(ProductoControllerV2.class).obtenerTodos()).withRel("todos-productos"));
    }
}
