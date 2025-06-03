package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Notificacion;
import com.letrasypapeles.backend.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerTodas() {
        List<Notificacion> notificaciones = notificacionService.obtenerTodas();
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtenerPorId(@PathVariable Long id) {
        return notificacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Notificacion>> obtenerPorClienteId(@PathVariable Long clienteId) {
        List<Notificacion> notificaciones = notificacionService.obtenerPorClienteId(clienteId);
        return ResponseEntity.ok(notificaciones);
    }

    @PostMapping
    public ResponseEntity<Notificacion> crearNotificacion(@RequestBody Notificacion notificacion) {
        notificacion.setFecha(LocalDateTime.now());
        Notificacion nuevaNotificacion = notificacionService.guardar(notificacion);
        return ResponseEntity.ok(nuevaNotificacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        return notificacionService.obtenerPorId(id)
                .map(n -> {
                    notificacionService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
