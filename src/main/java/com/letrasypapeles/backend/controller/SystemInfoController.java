package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.config.SwaggerSecurityProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@Tag(name = "Sistema", description = "Endpoints informativos del sistema")
public class SystemInfoController {

    @Autowired
    private SwaggerSecurityProperties swaggerSecurityProperties;

    @Operation(
        summary = "Información de configuración de Swagger",
        description = "Devuelve información sobre el estado actual de la seguridad de Swagger según el perfil activo",
        responses = {
            @ApiResponse(responseCode = "200", description = "Información obtenida exitosamente")
        }
    )
    @GetMapping("/swagger-config")
    public ResponseEntity<Map<String, Object>> getSwaggerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("swaggerSecurityEnabled", swaggerSecurityProperties.isSecurityEnabled());
        config.put("requiredAuthority", swaggerSecurityProperties.getRequiredAuthority());
        config.put("accessDeniedMessage", swaggerSecurityProperties.getAccessDeniedMessage());
        
        if (swaggerSecurityProperties.isSecurityEnabled()) {
            config.put("mode", "PROTEGIDO");
            config.put("description", "Swagger requiere autenticación con JWT y autoridad: " + 
                     swaggerSecurityProperties.getRequiredAuthority());
        } else {
            config.put("mode", "PÚBLICO");
            config.put("description", "Swagger es accesible públicamente (modo desarrollo)");
        }
        
        return ResponseEntity.ok(config);
    }

    @Operation(
        summary = "Estado del sistema",
        description = "Información general del estado de la aplicación",
        responses = {
            @ApiResponse(responseCode = "200", description = "Sistema funcionando correctamente")
        }
    )
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("application", "Letras y Papeles API");
        status.put("version", "1.0.0");
        return ResponseEntity.ok(status);
    }
}
