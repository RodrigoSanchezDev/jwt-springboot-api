package com.letrasypapeles.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.support.WebStack;

/**
 * Configuración HATEOAS para el proyecto
 * Habilita el soporte completo de hipermedia para REST APIs
 */
@Configuration
@EnableHypermediaSupport(
        type = {EnableHypermediaSupport.HypermediaType.HAL, EnableHypermediaSupport.HypermediaType.HAL_FORMS},
        stacks = WebStack.WEBMVC
)
public class HateoasConfig {
    
    // Esta configuración habilita:
    // - HAL (Hypertext Application Language) para enlaces hipermedia
    // - HAL-FORMS para formularios dinámicos en respuestas
    // - Soporte completo para WebMVC stack
    
}
