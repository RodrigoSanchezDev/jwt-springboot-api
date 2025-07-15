package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.service.RoleService;
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

@Tag(name = "Roles", description = "Gestión de roles y permisos del sistema")
@RestController
@RequestMapping("/api/roles")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "Obtener todos los roles", description = "Retorna una lista de todos los roles disponibles en el sistema")
    @GetMapping
    public ResponseEntity<List<Role>> obtenerTodos() {
        List<Role> roles = roleService.obtenerTodos();
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Obtener role por nombre", description = "Retorna un role específico basado en su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role encontrado"),
        @ApiResponse(responseCode = "404", description = "Role no encontrado")
    })
    @GetMapping("/{nombre}")
    public ResponseEntity<Role> obtenerPorNombre(
            @Parameter(description = "Nombre del role", example = "ADMIN")
            @PathVariable String nombre) {
        return roleService.obtenerPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo role", description = "Registra un nuevo role en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<Role> crearRole(@RequestBody Role role) {
        Role nuevoRole = roleService.guardar(role);
        return ResponseEntity.ok(nuevoRole);
    }

    @Operation(summary = "Eliminar role", description = "Elimina un role del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Role no encontrado")
    })
    @DeleteMapping("/{nombre}")
    public ResponseEntity<Void> eliminarRole(
            @Parameter(description = "Nombre del role", example = "ADMIN")
            @PathVariable String nombre) {
        return roleService.obtenerPorNombre(nombre)
                .map(r -> {
                    roleService.eliminar(nombre);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
