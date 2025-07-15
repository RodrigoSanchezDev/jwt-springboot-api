package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.service.InventarioService;
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

@Tag(name = "Inventario", description = "Gestión del inventario de productos por sucursal")
@RestController
@RequestMapping("/api/inventarios")
@SecurityRequirement(name = "bearerAuth")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Operation(summary = "Obtener todos los inventarios", description = "Retorna una lista de todos los registros de inventario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de inventarios obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Inventario>> obtenerTodos() {
        List<Inventario> inventarios = inventarioService.obtenerTodos();
        return ResponseEntity.ok(inventarios);
    }

    @Operation(summary = "Obtener inventario por ID", description = "Retorna un registro de inventario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario encontrado"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerPorId(
            @Parameter(description = "ID del inventario", example = "1")
            @PathVariable Long id) {
        return inventarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener inventario por producto", description = "Retorna los inventarios de un producto específico")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Inventario>> obtenerPorProductoId(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long productoId) {
        List<Inventario> inventarios = inventarioService.obtenerPorProductoId(productoId);
        return ResponseEntity.ok(inventarios);
    }

    @Operation(summary = "Obtener inventario por sucursal", description = "Retorna los inventarios de una sucursal específica")
    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<List<Inventario>> obtenerPorSucursalId(
            @Parameter(description = "ID de la sucursal", example = "1")
            @PathVariable Long sucursalId) {
        List<Inventario> inventarios = inventarioService.obtenerPorSucursalId(sucursalId);
        return ResponseEntity.ok(inventarios);
    }

    @Operation(summary = "Crear nuevo inventario", description = "Crea un nuevo registro de inventario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<Inventario> crearInventario(@RequestBody Inventario inventario) {
        Inventario nuevoInventario = inventarioService.guardar(inventario);
        return ResponseEntity.ok(nuevoInventario);
    }

    @Operation(summary = "Actualizar inventario", description = "Actualiza un registro de inventario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarInventario(
            @Parameter(description = "ID del inventario", example = "1")
            @PathVariable Long id,
            @RequestBody Inventario inventario) {
        return inventarioService.obtenerPorId(id)
                .map(i -> {
                    inventario.setId(id);
                    Inventario inventarioActualizado = inventarioService.guardar(inventario);
                    return ResponseEntity.ok(inventarioActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar inventario", description = "Elimina un registro de inventario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInventario(
            @Parameter(description = "ID del inventario", example = "1")
            @PathVariable Long id) {
        return inventarioService.obtenerPorId(id)
                .map(i -> {
                    inventarioService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
