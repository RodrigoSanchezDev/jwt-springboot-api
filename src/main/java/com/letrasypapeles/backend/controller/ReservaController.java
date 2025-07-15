package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Reservas V1 (Deprecated)", description = "⚠️ DEPRECADO: Gestión de reservas de productos. Migre a /api/v2/reservas para soporte HATEOAS")
@RestController
@RequestMapping("/api/reservas")
@SecurityRequirement(name = "bearerAuth")
@Deprecated
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Operation(summary = "Obtener todas las reservas", description = "Retorna una lista de todas las reservas registradas")
    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerTodas() {
        List<Reserva> reservas = reservaService.obtenerTodas();
        return ResponseEntity.ok(reservas);
    }

    @Operation(summary = "Obtener reserva por ID", description = "Retorna una reserva específica basada en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerPorId(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long id) {
        return reservaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener reservas por cliente", description = "Retorna todas las reservas de un cliente específico")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Reserva>> obtenerPorClienteId(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long clienteId) {
        List<Reserva> reservas = reservaService.obtenerPorClienteId(clienteId);
        return ResponseEntity.ok(reservas);
    }

    @Operation(summary = "Crear nueva reserva", description = "Crea una nueva reserva de producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<Reserva> crearReserva(@RequestBody Reserva reserva) {
        Reserva nuevaReserva = reservaService.guardar(reserva);
        return ResponseEntity.ok(nuevaReserva);
    }

    @Operation(summary = "Actualizar reserva", description = "Actualiza una reserva existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizarReserva(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long id, 
            @RequestBody Reserva reserva) {
        return reservaService.obtenerPorId(id)
                .map(r -> {
                    reserva.setId(id);
                    Reserva reservaActualizada = reservaService.guardar(reserva);
                    return ResponseEntity.ok(reservaActualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar reserva", description = "Elimina una reserva del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(
            @Parameter(description = "ID de la reserva", example = "1")
            @PathVariable Long id) {
        return reservaService.obtenerPorId(id)
                .map(r -> {
                    reservaService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
