package com.letrasypapeles.backend.assembler;

import com.letrasypapeles.backend.controller.v2.ClienteControllerV2;
import com.letrasypapeles.backend.entity.Cliente;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Assembler HATEOAS para la entidad Cliente
 * Convierte entidades Cliente en EntityModel con enlaces hipermedia
 */
@Component
public class ClienteRepresentationModelAssembler implements RepresentationModelAssembler<Cliente, EntityModel<Cliente>> {

    @Override
    @NonNull
    public EntityModel<Cliente> toModel(@NonNull Cliente cliente) {
        // Limpiar datos sensibles antes de serializar
        Cliente clienteLimpio = Cliente.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .email(cliente.getEmail())
                .puntosFidelidad(cliente.getPuntosFidelidad())
                .roles(cliente.getRoles())
                .build();

        return EntityModel.of(clienteLimpio)
                // Self link - enlace al recurso actual
                .add(linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(cliente.getId())).withSelfRel())
                // Enlaces de navegación
                .add(linkTo(methodOn(ClienteControllerV2.class).obtenerTodos()).withRel("clientes"))
                // Enlaces de acciones
                .add(linkTo(methodOn(ClienteControllerV2.class).actualizarCliente(cliente.getId(), null)).withRel("edit"))
                .add(linkTo(methodOn(ClienteControllerV2.class).eliminarCliente(cliente.getId())).withRel("delete"));
    }
}
