package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Sucursal;
import com.letrasypapeles.backend.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<Sucursal>> obtenerTodas() {
        List<Sucursal> sucursales = sucursalService.obtenerTodas();
        return ResponseEntity.ok(sucursales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> obtenerPorId(@PathVariable Long id) {
        return sucursalService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sucursal> crearSucursal(@RequestBody Sucursal sucursal) {
        Sucursal nuevaSucursal = sucursalService.guardar(sucursal);
        return ResponseEntity.ok(nuevaSucursal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> actualizarSucursal(@PathVariable Long id, @RequestBody Sucursal sucursal) {
        return sucursalService.obtenerPorId(id)
                .map(s -> {
                    sucursal.setId(id);
                    Sucursal sucursalActualizada = sucursalService.guardar(sucursal);
                    return ResponseEntity.ok(sucursalActualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable Long id) {
        return sucursalService.obtenerPorId(id)
                .map(s -> {
                    sucursalService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
