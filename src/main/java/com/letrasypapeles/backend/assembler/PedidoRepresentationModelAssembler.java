package com.letrasypapeles.backend.assembler;

import com.letrasypapeles.backend.controller.PedidoController;
import com.letrasypapeles.backend.controller.ClienteController;
import com.letrasypapeles.backend.entity.Pedido;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler HATEOAS para la entidad Pedido
 * Convierte entidades Pedido en EntityModel con enlaces hipermedia
 */
@Component
public class PedidoRepresentationModelAssembler implements RepresentationModelAssembler<Pedido, EntityModel<Pedido>> {

    @Override
    @NonNull
    public EntityModel<Pedido> toModel(@NonNull Pedido pedido) {
        return EntityModel.of(pedido)
                // Self link - enlace al recurso actual
                .add(linkTo(methodOn(PedidoController.class).obtenerPorId(pedido.getId())).withSelfRel())
                // Enlaces de navegación
                .add(linkTo(methodOn(PedidoController.class).obtenerTodos()).withRel("pedidos"))
                // Enlaces de acciones
                .add(linkTo(methodOn(PedidoController.class).actualizarPedido(pedido.getId(), null)).withRel("edit"))
                .add(linkTo(methodOn(PedidoController.class).eliminarPedido(pedido.getId())).withRel("delete"))
                // Enlaces relacionados
                .add(linkTo(methodOn(ClienteController.class).obtenerPorId(pedido.getCliente().getId())).withRel("cliente"));
    }
}
