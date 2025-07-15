package com.letrasypapeles.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuración personalizada para controlar la seguridad de Swagger
 * según el perfil de Spring activo
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.swagger")
public class SwaggerSecurityProperties {
    
    /**
     * Indica si Swagger debe estar protegido por JWT
     * - true: Solo usuarios autenticados pueden acceder
     * - false: Acceso público (recomendado solo para desarrollo)
     */
    private boolean securityEnabled = false;
    
    /**
     * Autoridad/rol requerido para acceder a Swagger cuando está protegido
     * Valores típicos: "ADMIN", "DEVELOPER", "GERENTE"
     */
    private String requiredAuthority = "ADMIN";
    
    /**
     * Mensaje personalizado para acceso denegado a Swagger
     */
    private String accessDeniedMessage = "Acceso denegado: Se requiere autenticación para ver la documentación API";
}
