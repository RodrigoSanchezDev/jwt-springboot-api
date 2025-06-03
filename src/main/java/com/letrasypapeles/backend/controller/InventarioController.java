package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<Inventario>> obtenerTodos() {
        List<Inventario> inventarios = inventarioService.obtenerTodos();
        return ResponseEntity.ok(inventarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerPorId(@PathVariable Long id) {
        return inventarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Inventario>> obtenerPorProductoId(@PathVariable Long productoId) {
        List<Inventario> inventarios = inventarioService.obtenerPorProductoId(productoId);
        return ResponseEntity.ok(inventarios);
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Inventario>> obtenerPorSucursalId(@PathVariable Long sucursalId) {
        List<Inventario> inventarios = inventarioService.obtenerPorSucursalId(sucursalId);
        return ResponseEntity.ok(inventarios);
    }

    @PostMapping
    public ResponseEntity<Inventario> crearInventario(@RequestBody Inventario inventario) {
        Inventario nuevoInventario = inventarioService.guardar(inventario);
        return ResponseEntity.ok(nuevoInventario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarInventario(@PathVariable Long id, @RequestBody Inventario inventario) {
        return inventarioService.obtenerPorId(id)
                .map(i -> {
                    inventario.setId(id);
                    Inventario inventarioActualizado = inventarioService.guardar(inventario);
                    return ResponseEntity.ok(inventarioActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Long id) {
        return inventarioService.obtenerPorId(id)
                .map(i -> {
                    inventarioService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
