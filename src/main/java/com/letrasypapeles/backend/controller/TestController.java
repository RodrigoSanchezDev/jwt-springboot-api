package com.letrasypapeles.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test de Autorización", description = "Endpoints para probar diferentes niveles de autorización por rol")
@RestController
@RequestMapping("/api/test")
@SecurityRequirement(name = "bearerAuth")
public class TestController {

    @Operation(
        summary = "Endpoint solo para CLIENTE",
        description = "Endpoint de prueba accesible únicamente para usuarios con rol CLIENTE"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Acceso autorizado para CLIENTE"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - rol insuficiente")
    })
    @GetMapping("/cliente")
    @PreAuthorize("hasAuthority('CLIENTE')")
    public String soloCliente() {
        return "¡Hola CLIENTE!";
    }

    @Operation(
        summary = "Endpoint solo para EMPLEADO",
        description = "Endpoint de prueba accesible únicamente para usuarios con rol EMPLEADO"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Acceso autorizado para EMPLEADO"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - rol insuficiente")
    })
    @GetMapping("/empleado")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    public String soloEmpleado() {
        return "¡Hola EMPLEADO!";
    }

    @Operation(
        summary = "Endpoint solo para GERENTE",
        description = "Endpoint de prueba accesible únicamente para usuarios con rol GERENTE"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Acceso autorizado para GERENTE"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - rol insuficiente")
    })
    @GetMapping("/gerente")
    @PreAuthorize("hasAuthority('GERENTE')")
    public String soloGerente() {
        return "¡Hola GERENTE!";
    }

    @Operation(
        summary = "Endpoint solo para ADMIN",
        description = "Endpoint de prueba accesible únicamente para usuarios con rol ADMIN (máximo privilegio)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Acceso autorizado para ADMIN"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - rol insuficiente")
    })
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String admin() {
        return "Hola ADMIN, este endpoint es sólo para ti.";
    }
}
