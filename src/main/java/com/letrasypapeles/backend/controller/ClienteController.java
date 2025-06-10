package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodos() {
        List<Cliente> clientes = clienteService.obtenerTodos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(cliente -> {
                    cliente.setContraseña(null); 
                    return ResponseEntity.ok(cliente);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registro")
    public ResponseEntity<Cliente> registrarCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.registrarCliente(cliente);
        nuevoCliente.setContraseña(null); 
        return ResponseEntity.ok(nuevoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.obtenerPorId(id)
                .map(c -> {
                    cliente.setId(id);
                    Cliente clienteActualizado = clienteService.actualizarCliente(cliente);
                    clienteActualizado.setContraseña(null);
                    return ResponseEntity.ok(clienteActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(c -> {
                    clienteService.eliminar(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
