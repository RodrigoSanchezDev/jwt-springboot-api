package com.letrasypapeles.backend.security;

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

@Configuration
@EnableMethodSecurity  
@RequiredArgsConstructor
public class SecurityConfig {

    private final UsuarioService usuarioService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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

    // Cadena de filtros + reglas de autorización
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1) API REST + JWT = stateless
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 2) rutas públicas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()

                // Endpoints exclusivos por rol
                .requestMatchers("/api/cliente/**").hasAuthority("CLIENTE")
                .requestMatchers("/api/empleado/**").hasAuthority("EMPLEADO")
                .requestMatchers("/api/gerente/**").hasAuthority("GERENTE")
                .requestMatchers("/api/admin/**").hasAuthority("ADMIN")

                // El resto requiere autenticación
                .anyRequest().authenticated())

            // 5) para consola H2 en dev
            .headers(h -> h.frameOptions(fr -> fr.disable()))

            // 6) provider + filtro JWT
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter,
                             UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
