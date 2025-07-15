package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Sucursal;
import com.letrasypapeles.backend.service.SucursalService;
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

@Tag(name = "Sucursales", description = "Gestión de sucursales de la empresa")
@RestController
@RequestMapping("/api/sucursales")
@SecurityRequirement(name = "bearerAuth")
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @Operation(summary = "Obtener todas las sucursales", description = "Retorna una lista de todas las sucursales registradas")
    @GetMapping
    public ResponseEntity<List<Sucursal>> obtenerTodas() {
        List<Sucursal> sucursales = sucursalService.obtenerTodas();
        return ResponseEntity.ok(sucursales);
    }

    @Operation(summary = "Obtener sucursal por ID", description = "Retorna una sucursal específica basada en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> obtenerPorId(
            @Parameter(description = "ID de la sucursal", example = "1")
            @PathVariable Long id) {
        return sucursalService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nueva sucursal", description = "Registra una nueva sucursal en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<Sucursal> crearSucursal(@RequestBody Sucursal sucursal) {
        Sucursal nuevaSucursal = sucursalService.guardar(sucursal);
        return ResponseEntity.ok(nuevaSucursal);
    }

    @Operation(summary = "Actualizar sucursal", description = "Actualiza la información de una sucursal existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> actualizarSucursal(
            @Parameter(description = "ID de la sucursal", example = "1")
            @PathVariable Long id, 
            @RequestBody Sucursal sucursal) {
        return sucursalService.obtenerPorId(id)
                .map(s -> {
                    sucursal.setId(id);
                    Sucursal sucursalActualizada = sucursalService.guardar(sucursal);
                    return ResponseEntity.ok(sucursalActualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSucursal(
            @Parameter(description = "ID de la sucursal", example = "1")
            @PathVariable Long id) {
        return sucursalService.obtenerPorId(id)
                .map(s -> {
                    sucursalService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
