package com.letrasypapeles.backend.security;

import com.letrasypapeles.backend.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Ignorar rutas públicas para el filtro JWT
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/") || path.startsWith("/h2-console/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1) Capturamos el header "Authorization"
        final String header = request.getHeader("Authorization");

        String email = null;
        String token = null;

        // 2) Comprobamos que empiece con "Bearer " y tenga longitud mínima
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7); // quitamos "Bearer "
            // Validar que el token tenga una longitud mínima razonable
            if (token != null && token.length() > 10) {
                try {
                    email = jwtUtil.extractUsername(token);
                } catch (Exception ex) {
                    // Token inválido o expirado: dejamos que pase al filterChain,
                    // y Spring devolverá 401 si intenta acceder a endpoint protegido.
                    logger.warn("JWT inválido: " + ex.getMessage());
                }
            }
        }

        // 3) Si extrajimos un email y aún no hay autenticación en el contexto:
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Cargamos UserDetails usando nuestro service que implementa UserDetailsService
                UserDetails userDetails = usuarioService.loadUserByUsername(email);

                // Extraer authorities del token y reconstruir UserDetails con authorities del JWT
                List<String> roles = jwtUtil.extractAuthorities(token);
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                UserDetails userDetailsWithRoles = new org.springframework.security.core.userdetails.User(
                        userDetails.getUsername(), userDetails.getPassword(), authorities
                );

                // 4) Si el token es válido para ese usuario, construimos un Authentication
                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetailsWithRoles, null, userDetailsWithRoles.getAuthorities());

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ex) {
                // Error al cargar el usuario: continuar sin autenticación
                logger.warn("Error cargando usuario: " + ex.getMessage());
            }
        }

        
        filterChain.doFilter(request, response);
    }
}
