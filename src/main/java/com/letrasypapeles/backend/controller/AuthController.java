package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
import com.letrasypapeles.backend.service.UsuarioService;
import com.letrasypapeles.backend.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    // DTO para el request de login
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        private String email;
        private String contra; 
    }

    // DTO para la respuesta de login (token)
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LoginResponse {
        private String token;
    }

    // DTO para el request de registro con rol
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class RegisterRequest {
        private String nombre;
        private String apellido;
        private String email;
        private String contraseña;
        private String rol; // CLIENTE, EMPLEADO, GERENTE, ADMIN
    }

    /**
     * Endpoint para registrar un nuevo Cliente.
     * Ahora acepta un JSON con nombre, apellido, email, contraseña y rol.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setEmail(request.getEmail());
        cliente.setContraseña(request.getContraseña());
        // El rol se maneja en ClienteService
        Cliente creado = clienteService.registrarClienteConRol(cliente, request.getRol());
        creado.setContraseña(null);
        return ResponseEntity.ok(creado);
    }

    /**
     * Endpoint para hacer login. Recibe email + contraseña en texto plano.
     * Si la autenticación es exitosa, genera un JWT y lo devuelve.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // Esto usa internamente UsuarioService (que implementa UserDetailsService) + PasswordEncoder
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getContra())
        );

        // Recargar UserDetails para asegurar que los authorities estén completos
        UserDetails userDetails = usuarioService.loadUserByUsername(request.getEmail());
        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}
