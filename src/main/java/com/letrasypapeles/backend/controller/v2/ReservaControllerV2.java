package com.letrasypapeles.backend.controller.v2;

import com.letrasypapeles.backend.assembler.ReservaRepresentationModelAssembler;
import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.service.ReservaService;
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
 * Controlador REST V2 para gestión de reservas con soporte HATEOAS
 * Proporciona navegación hipermedia y enlaces dinámicos
 * 
 * @author Rodrigo Sanchez
 * @version 2.0.0
 */
@Tag(name = "Reservas V2 (HATEOAS)", description = "API V2 - Gestión de reservas con navegación hipermedia y enlaces dinámicos")
@RestController
@RequestMapping("/api/v2/reservas")
@SecurityRequirement(name = "bearerAuth")
public class ReservaControllerV2 {

    @Autowired
    private ReservaService reservaService;
    
    @Autowired
    private ReservaRepresentationModelAssembler reservaAssembler;

    @Operation(
        summary = "Obtener todas las reservas (HATEOAS)", 
        description = "Retorna una colección de reservas con enlaces hipermedia para navegación y acciones disponibles. " +
                     "Incluye enlaces self, navegación a recursos relacionados y acciones CRUD."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Colección de reservas obtenida exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/CollectionModel"))
        )
    })
    @GetMapping
    public CollectionModel<EntityModel<Reserva>> obtenerTodas() {
        List<EntityModel<Reserva>> reservas = reservaService.obtenerTodas().stream()
                .map(reservaAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas)
                .add(linkTo(methodOn(ReservaControllerV2.class).obtenerTodas()).withSelfRel())
                .add(linkTo(methodOn(ReservaControllerV2.class).crearReserva(null)).withRel("create"))
                .add(linkTo(methodOn(ReservaControllerV2.class).obtenerTodas()).withRel("refresh"));
    }

    @Operation(
        summary = "Obtener reserva por ID (HATEOAS)", 
        description = "Retorna una reserva específica con enlaces hipermedia a recursos relacionados y acciones disponibles"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Reserva encontrada con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Reserva>> obtenerPorId(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long id) {
        return reservaService.obtenerPorId(id)
                .map(reserva -> ResponseEntity.ok(reservaAssembler.toModel(reserva)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nueva reserva (HATEOAS)", 
        description = "Crea una nueva reserva y retorna la entidad con enlaces hipermedia"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Reserva creada exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<EntityModel<Reserva>> crearReserva(@RequestBody Reserva reserva) {
        Reserva nuevaReserva = reservaService.guardar(reserva);
        
        EntityModel<Reserva> reservaModel = reservaAssembler.toModel(nuevaReserva);
        
        return ResponseEntity
                .created(URI.create(reservaModel.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                .body(reservaModel);
    }

    @Operation(
        summary = "Actualizar reserva (HATEOAS)", 
        description = "Actualiza una reserva existente y retorna la entidad con enlaces hipermedia actualizados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Reserva actualizada exitosamente con enlaces HATEOAS",
            content = @Content(schema = @Schema(ref = "#/components/schemas/EntityModel"))
        ),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Reserva>> actualizarReserva(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long id, 
            @RequestBody Reserva reserva) {
        return reservaService.obtenerPorId(id)
                .map(reservaExistente -> {
                    reserva.setId(id);
                    Reserva reservaActualizada = reservaService.guardar(reserva);
                    return ResponseEntity.ok(reservaAssembler.toModel(reservaActualizada));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Eliminar reserva (HATEOAS)", 
        description = "Elimina una reserva y retorna enlaces para navegar de vuelta a la colección"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reserva eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long id) {
        return reservaService.obtenerPorId(id)
                .map(reserva -> {
                    reservaService.eliminar(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Obtener reservas por cliente (HATEOAS)",
        description = "Retorna todas las reservas de un cliente específico con enlaces hipermedia"
    )
    @GetMapping("/cliente/{clienteId}")
    public CollectionModel<EntityModel<Reserva>> obtenerPorCliente(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long clienteId) {
        List<EntityModel<Reserva>> reservas = reservaService.obtenerPorClienteId(clienteId).stream()
                .map(reservaAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas)
                .add(linkTo(methodOn(ReservaControllerV2.class).obtenerPorCliente(clienteId)).withSelfRel())
                .add(linkTo(methodOn(ReservaControllerV2.class).obtenerTodas()).withRel("todas-reservas"))
                .add(linkTo(methodOn(ClienteControllerV2.class).obtenerPorId(clienteId)).withRel("cliente"));
    }

    @Operation(
        summary = "Obtener reservas por estado (HATEOAS)",
        description = "Retorna reservas filtradas por estado con enlaces hipermedia"
    )
    @GetMapping("/estado/{estado}")
    public CollectionModel<EntityModel<Reserva>> obtenerPorEstado(
            @Parameter(description = "Estado de la reserva", example = "ACTIVA")
            @PathVariable String estado) {
        List<EntityModel<Reserva>> reservas = reservaService.obtenerTodas().stream()
                .filter(r -> r.getEstado().equalsIgnoreCase(estado))
                .map(reservaAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(reservas)
                .add(linkTo(methodOn(ReservaControllerV2.class).obtenerPorEstado(estado)).withSelfRel())
                .add(linkTo(methodOn(ReservaControllerV2.class).obtenerTodas()).withRel("todas-reservas"));
    }
}
