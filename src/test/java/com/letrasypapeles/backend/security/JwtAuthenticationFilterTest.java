package com.letrasypapeles.backend.security;

import com.letrasypapeles.backend.config.SwaggerSecurityProperties;
import com.letrasypapeles.backend.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private SwaggerSecurityProperties swaggerSecurityProperties;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Mock SwaggerSecurityProperties con valor por defecto
        when(swaggerSecurityProperties.isSecurityEnabled()).thenReturn(true);
        
        userDetails = User.builder()
                .username("test@example.com")
                .password("hashedPassword")
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("CLIENTE")))
                .build();
    }

    @Test
    void testDoFilterInternalWithValidToken() throws ServletException, IOException {
        // Arrange
        String token = "valid.jwt.token";
        String email = "test@example.com";
        
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(email);
        when(jwtUtil.validateToken(token, userDetails)).thenReturn(true);
        when(usuarioService.loadUserByUsername(email)).thenReturn(userDetails);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil).extractUsername(token);
        verify(usuarioService).loadUserByUsername(email);
        verify(jwtUtil).validateToken(token, userDetails);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithoutAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(usuarioService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithInvalidTokenFormat() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(usuarioService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithInvalidToken() throws ServletException, IOException {
        // Arrange
        String token = "invalid.jwt.token";
        String email = "test@example.com";
        
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(email);
        when(jwtUtil.validateToken(token, userDetails)).thenReturn(false);
        when(usuarioService.loadUserByUsername(email)).thenReturn(userDetails);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil).extractUsername(token);
        verify(usuarioService).loadUserByUsername(email);
        verify(jwtUtil).validateToken(token, userDetails);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithNullUsername() throws ServletException, IOException {
        // Arrange
        String token = "token.with.null.username";
        
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil).extractUsername(token);
        verify(usuarioService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithExpiredToken() throws ServletException, IOException {
        // Arrange
        String token = "expired.jwt.token";
        
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractUsername(token)).thenThrow(new RuntimeException("Token expired"));

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil).extractUsername(token);
        verify(usuarioService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternalWithPublicRoute() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(usuarioService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }
    
    @Test
    void testDoFilterInternalWithEmptyAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getHeader("Authorization")).thenReturn("");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(usuarioService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }
    
    @Test
    void testDoFilterInternalWithBearerButNoToken() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(usuarioService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }
    
    @Test
    void testDoFilterInternalWithBearerAndShortToken() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getHeader("Authorization")).thenReturn("Bearer x");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(usuarioService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);
    }
}
