package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
import com.letrasypapeles.backend.service.UsuarioService;
import com.letrasypapeles.backend.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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


@Tag(name = "Autenticación", description = "Operaciones de autenticación y registro de usuarios")
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
    @Schema(description = "Datos de inicio de sesión")
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LoginRequest {
        @Schema(description = "Email del usuario", example = "usuario@email.com")
        private String email;
        
        @Schema(description = "Contraseña del usuario", example = "password123")
        private String contra; 
    }

    // DTO para la respuesta de login (token)
    @Schema(description = "Respuesta de inicio de sesión exitoso")
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LoginResponse {
        @Schema(description = "Token JWT de autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        private String token;
    }

    // DTO para el request de registro con rol
    @Schema(description = "Datos para el registro de un nuevo usuario")
    @Data @NoArgsConstructor @AllArgsConstructor
    public static class RegisterRequest {
        @Schema(description = "Nombre del usuario", example = "Juan")
        @NotEmpty(message = "El nombre es obligatorio")
        private String nombre;
        
        @Schema(description = "Apellido del usuario", example = "Pérez")
        @NotEmpty(message = "El apellido es obligatorio")
        private String apellido;
        
        @Schema(description = "Email del usuario", example = "juan.perez@email.com")
        @Email(message = "El email debe tener un formato válido")
        @NotEmpty(message = "El email es obligatorio")
        private String email;
        
        @Schema(description = "Contraseña del usuario", example = "password123")
        @NotEmpty(message = "La contraseña es obligatoria")
        private String contraseña;
        
        @Schema(description = "Rol del usuario", example = "CLIENTE", allowableValues = {"CLIENTE", "EMPLEADO", "GERENTE", "ADMIN"})
        @NotEmpty(message = "El rol es obligatorio")
        private String rol; // CLIENTE, EMPLEADO, GERENTE, ADMIN
    }

    /**
     * Endpoint para registrar un nuevo Cliente.
     * Ahora acepta un JSON con nombre, apellido, email, contraseña y rol.
     */
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Registra un nuevo usuario en el sistema con el rol especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Usuario registrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Cliente.class),
                examples = @ExampleObject(
                    value = "{\"id\": 1, \"nombre\": \"Juan\", \"apellido\": \"Pérez\", \"email\": \"juan.perez@email.com\", \"puntosFidelidad\": 0}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\": \"El email ya está registrado\"}"
                )
            )
        )
    })
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
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario y devuelve un token JWT para acceder a los endpoints protegidos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Inicio de sesión exitoso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginResponse.class),
                examples = @ExampleObject(
                    value = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciales inválidas",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\": \"Invalid credentials\"}"
                )
            )
        )
    })
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
