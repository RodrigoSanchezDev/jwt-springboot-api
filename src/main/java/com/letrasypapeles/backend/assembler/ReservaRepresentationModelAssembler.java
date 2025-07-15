package com.letrasypapeles.backend.assembler;

import com.letrasypapeles.backend.controller.ReservaController;
import com.letrasypapeles.backend.controller.ClienteController;
import com.letrasypapeles.backend.controller.ProductoController;
import com.letrasypapeles.backend.entity.Reserva;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler HATEOAS para la entidad Reserva
 * Convierte entidades Reserva en EntityModel con enlaces hipermedia
 */
@Component
public class ReservaRepresentationModelAssembler implements RepresentationModelAssembler<Reserva, EntityModel<Reserva>> {

    @Override
    @NonNull
    public EntityModel<Reserva> toModel(@NonNull Reserva reserva) {
        return EntityModel.of(reserva)
                // Self link - enlace al recurso actual
                .add(linkTo(methodOn(ReservaController.class).obtenerPorId(reserva.getId())).withSelfRel())
                // Enlaces de navegación
                .add(linkTo(methodOn(ReservaController.class).obtenerTodas()).withRel("reservas"))
                // Enlaces de acciones
                .add(linkTo(methodOn(ReservaController.class).actualizarReserva(reserva.getId(), null)).withRel("edit"))
                .add(linkTo(methodOn(ReservaController.class).eliminarReserva(reserva.getId())).withRel("delete"))
                // Enlaces relacionados
                .add(linkTo(methodOn(ClienteController.class).obtenerPorId(reserva.getCliente().getId())).withRel("cliente"))
                .add(linkTo(methodOn(ProductoController.class).obtenerPorId(reserva.getProducto().getId())).withRel("producto"));
    }
}
