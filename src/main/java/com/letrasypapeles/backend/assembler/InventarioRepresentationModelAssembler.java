package com.letrasypapeles.backend.assembler;

import com.letrasypapeles.backend.controller.InventarioController;
import com.letrasypapeles.backend.controller.ProductoController;
import com.letrasypapeles.backend.controller.SucursalController;
import com.letrasypapeles.backend.entity.Inventario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler HATEOAS para la entidad Inventario
 * Convierte entidades Inventario en EntityModel con enlaces hipermedia
 */
@Component
public class InventarioRepresentationModelAssembler implements RepresentationModelAssembler<Inventario, EntityModel<Inventario>> {

    @Override
    @NonNull
    public EntityModel<Inventario> toModel(@NonNull Inventario inventario) {
        return EntityModel.of(inventario)
                // Self link - enlace al recurso actual
                .add(linkTo(methodOn(InventarioController.class).obtenerPorId(inventario.getId())).withSelfRel())
                // Enlaces de navegación
                .add(linkTo(methodOn(InventarioController.class).obtenerTodos()).withRel("inventarios"))
                // Enlaces de acciones
                .add(linkTo(methodOn(InventarioController.class).actualizarInventario(inventario.getId(), null)).withRel("edit"))
                .add(linkTo(methodOn(InventarioController.class).eliminarInventario(inventario.getId())).withRel("delete"))
                // Enlaces relacionados
                .add(linkTo(methodOn(ProductoController.class).obtenerPorId(inventario.getProducto().getId())).withRel("producto"))
                .add(linkTo(methodOn(SucursalController.class).obtenerPorId(inventario.getSucursal().getId())).withRel("sucursal"));
    }
}
