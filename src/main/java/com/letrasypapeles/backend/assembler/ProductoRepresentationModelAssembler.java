package com.letrasypapeles.backend.assembler;

import com.letrasypapeles.backend.controller.ProductoController;
import com.letrasypapeles.backend.entity.Producto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler HATEOAS para la entidad Producto
 * Convierte entidades Producto en EntityModel con enlaces hipermedia
 */
@Component
public class ProductoRepresentationModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    @NonNull
    public EntityModel<Producto> toModel(@NonNull Producto producto) {
        return EntityModel.of(producto)
                // Self link - enlace al recurso actual
                .add(linkTo(methodOn(ProductoController.class).obtenerPorId(producto.getId())).withSelfRel())
                // Enlaces de navegación
                .add(linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("productos"))
                // Enlaces de acciones
                .add(linkTo(methodOn(ProductoController.class).actualizarProducto(producto.getId(), null)).withRel("edit"))
                .add(linkTo(methodOn(ProductoController.class).eliminarProducto(producto.getId())).withRel("delete"))
                // Enlaces relacionados
                .add(linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("categoria"))
                .add(linkTo(methodOn(ProductoController.class).obtenerTodos()).withRel("proveedor"));
    }
}
