package com.letrasypapeles.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/cliente")
    @PreAuthorize("hasAuthority('CLIENTE')")
    public String soloCliente() {
        return "¡Hola CLIENTE!";
    }

    @GetMapping("/empleado")
    @PreAuthorize("hasAuthority('EMPLEADO')")
    public String soloEmpleado() {
        return "¡Hola EMPLEADO!";
    }

    @GetMapping("/gerente")
    @PreAuthorize("hasAuthority('GERENTE')")
    public String soloGerente() {
        return "¡Hola GERENTE!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String admin() {
        return "Hola ADMIN, este endpoint es sólo para ti.";
    }
}
