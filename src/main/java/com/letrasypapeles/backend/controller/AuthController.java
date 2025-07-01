package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
import com.letrasypapeles.backend.service.UsuarioService;
import com.letrasypapeles.backend.security.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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
        @NotEmpty(message = "El nombre es obligatorio")
        private String nombre;
        
        @NotEmpty(message = "El apellido es obligatorio")
        private String apellido;
        
        @Email(message = "El email debe tener un formato válido")
        @NotEmpty(message = "El email es obligatorio")
        private String email;
        
        @NotEmpty(message = "La contraseña es obligatoria")
        private String contraseña;
        
        @NotEmpty(message = "El rol es obligatorio")
        private String rol; // CLIENTE, EMPLEADO, GERENTE, ADMIN
    }

    /**
     * Endpoint para registrar un nuevo Cliente.
     * Ahora acepta un JSON con nombre, apellido, email, contraseña y rol.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            Cliente cliente = new Cliente();
            cliente.setNombre(request.getNombre());
            cliente.setApellido(request.getApellido());
            cliente.setEmail(request.getEmail());
            cliente.setContraseña(request.getContraseña());
            // El rol se maneja en ClienteService
            Cliente creado = clienteService.registrarClienteConRol(cliente, request.getRol());
            if (creado != null) {
                creado.setContraseña(null);
            }
            return ResponseEntity.ok(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para hacer login. Recibe email + contraseña en texto plano.
     * Si la autenticación es exitosa, genera un JWT y lo devuelve.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Esto usa internamente UsuarioService (que implementa UserDetailsService) + PasswordEncoder
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getContra())
            );

            // Recargar UserDetails para asegurar que los authorities estén completos
            UserDetails userDetails = usuarioService.loadUserByUsername(request.getEmail());
            String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new LoginResponse(jwt));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
}
