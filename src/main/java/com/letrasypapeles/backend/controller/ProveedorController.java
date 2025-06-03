package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<Proveedor>> obtenerTodos() {
        List<Proveedor> proveedores = proveedorService.obtenerTodos();
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerPorId(@PathVariable Long id) {
        return proveedorService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Proveedor> crearProveedor(@RequestBody Proveedor proveedor) {
        Proveedor nuevoProveedor = proveedorService.guardar(proveedor);
        return ResponseEntity.ok(nuevoProveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizarProveedor(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        return proveedorService.obtenerPorId(id)
                .map(p -> {
                    proveedor.setId(id);
                    Proveedor proveedorActualizado = proveedorService.guardar(proveedor);
                    return ResponseEntity.ok(proveedorActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable Long id) {
        return proveedorService.obtenerPorId(id)
                .map(p -> {
                    proveedorService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
