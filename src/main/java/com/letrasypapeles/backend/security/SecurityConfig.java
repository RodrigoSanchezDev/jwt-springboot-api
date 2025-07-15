package com.letrasypapeles.backend.security;

import com.letrasypapeles.backend.config.SwaggerSecurityProperties;
import com.letrasypapeles.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableMethodSecurity  
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioService usuarioService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SwaggerSecurityProperties swaggerSecurityProperties;

    // BCrypt para cifrar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager que delega en AuthenticationProvider
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    // AuthenticationProvider -> UserDetailsService + PasswordEncoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Manejo personalizado de errores de autenticación
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                               AuthenticationException authException) throws IOException {
                // Verificar si es una ruta de Swagger/OpenAPI
                String requestURI = request.getRequestURI();
                if (requestURI.contains("/swagger") || requestURI.contains("/api-docs")) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                    response.setContentType("application/json");
                    response.getWriter().write(String.format(
                        "{\"error\":\"Unauthorized\",\"message\":\"%s\",\"path\":\"%s\",\"timestamp\":\"%s\"}",
                        swaggerSecurityProperties.getAccessDeniedMessage(),
                        requestURI,
                        java.time.Instant.now()
                    ));
                } else {
                    // Para otros endpoints, también devolver 401
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                    response.setContentType("application/json");
                    response.getWriter().write(String.format(
                        "{\"error\":\"Unauthorized\",\"message\":\"Se requiere autenticación JWT\",\"path\":\"%s\",\"timestamp\":\"%s\"}",
                        requestURI,
                        java.time.Instant.now()
                    ));
                }
            }
        };
    }

    // Cadena de filtros + reglas de autorización
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1) API REST + JWT = stateless
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 2) rutas públicas
            .authorizeHttpRequests(auth -> {
                // Configuración dinámica de Swagger según perfil
                if (swaggerSecurityProperties.isSecurityEnabled()) {
                    // MODO PRODUCCIÓN: Swagger protegido por JWT
                    auth.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html")
                        .hasAuthority(swaggerSecurityProperties.getRequiredAuthority())
                        .requestMatchers("/v3/api-docs/**", "/v3/api-docs.yaml", "/v3/api-docs")
                        .hasAuthority(swaggerSecurityProperties.getRequiredAuthority())
                        .requestMatchers("/api-docs/**", "/api-docs")
                        .hasAuthority(swaggerSecurityProperties.getRequiredAuthority());
                } else {
                    // MODO DESARROLLO: Swagger público
                    auth.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/v3/api-docs.yaml", "/v3/api-docs").permitAll()
                        .requestMatchers("/api-docs/**", "/api-docs").permitAll();
                }
                
                // Rutas adicionales de Swagger siempre públicas (recursos estáticos)
                auth.requestMatchers("/swagger-resources/**", "/webjars/**").permitAll()
                    .requestMatchers("/swagger-config", "/api-docs.yaml").permitAll()
                    // Otras rutas públicas
                    .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()

                    // Endpoints exclusivos por rol
                    .requestMatchers("/api/cliente/**").hasAuthority("CLIENTE")
                    .requestMatchers("/api/empleado/**").hasAuthority("EMPLEADO")
                    .requestMatchers("/api/gerente/**").hasAuthority("GERENTE")
                    .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

                    // El resto requiere autenticación
                    .anyRequest().authenticated();
            })

            // 3) Configurar AuthenticationEntryPoint personalizado
            .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint()))

            // 4) para consola H2 en dev
            .headers(h -> h.frameOptions(fr -> fr.disable()))

            // 5) provider + filtro JWT
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter,
                             UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
