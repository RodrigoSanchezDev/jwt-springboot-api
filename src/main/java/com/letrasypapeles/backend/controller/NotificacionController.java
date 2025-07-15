package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Notificacion;
import com.letrasypapeles.backend.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Notificaciones", description = "Gestión de notificaciones del sistema")
@RestController
@RequestMapping("/api/notificaciones")
@SecurityRequirement(name = "bearerAuth")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @Operation(summary = "Obtener todas las notificaciones", description = "Retorna una lista de todas las notificaciones registradas")
    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerTodas() {
        List<Notificacion> notificaciones = notificacionService.obtenerTodas();
        return ResponseEntity.ok(notificaciones);
    }

    @Operation(summary = "Obtener notificación por ID", description = "Retorna una notificación específica basada en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtenerPorId(
            @Parameter(description = "ID de la notificación", example = "1")
            @PathVariable Long id) {
        return notificacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener notificaciones por cliente", description = "Retorna todas las notificaciones de un cliente específico")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Notificacion>> obtenerPorClienteId(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long clienteId) {
        List<Notificacion> notificaciones = notificacionService.obtenerPorClienteId(clienteId);
        return ResponseEntity.ok(notificaciones);
    }

    @Operation(summary = "Crear nueva notificación", description = "Crea una nueva notificación en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<Notificacion> crearNotificacion(@RequestBody Notificacion notificacion) {
        notificacion.setFecha(LocalDateTime.now());
        Notificacion nuevaNotificacion = notificacionService.guardar(notificacion);
        return ResponseEntity.ok(nuevaNotificacion);
    }

    @Operation(summary = "Marcar notificación como leída", description = "Actualiza el estado de una notificación a leída")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación marcada como leída"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PutMapping("/{id}/marcar-leida")
    public ResponseEntity<Notificacion> marcarComoLeida(
            @Parameter(description = "ID de la notificación", example = "1")
            @PathVariable Long id) {
        return notificacionService.obtenerPorId(id)
                .map(notificacion -> {
                    notificacion.setLeida(true);
                    Notificacion notificacionActualizada = notificacionService.guardar(notificacion);
                    return ResponseEntity.ok(notificacionActualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(
            @Parameter(description = "ID de la notificación", example = "1")
            @PathVariable Long id) {
        return notificacionService.obtenerPorId(id)
                .map(n -> {
                    notificacionService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
