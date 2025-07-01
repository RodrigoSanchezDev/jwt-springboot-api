package com.letrasypapeles.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilTest {

    @InjectMocks
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Configurar valores requeridos para el test
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtSecret", "testSecretKeyForJunitTestsThisIsAVeryLongSecretKeyForTesting123456789");
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtExpirationMs", 3600000L);
    }

    @Test
    void testGenerateToken() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtTokenUtil.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 0);
        
        // Verificar que el token contiene partes (header.payload.signature)
        String[] tokenParts = token.split("\\.");
        assertEquals(3, tokenParts.length);
    }

    @Test
    void testGetUsernameFromToken() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtTokenUtil.generateToken(userDetails);
        String extractedUsername = jwtTokenUtil.getUsernameFromToken(token);

        assertEquals("testuser", extractedUsername);
    }

    @Test
    void testValidateToken() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtTokenUtil.generateToken(userDetails);
        boolean isValid = jwtTokenUtil.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testExpiredToken() {
        // Configurar expiración muy corta para simular token expirado
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtExpirationMs", 1L);
        
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtTokenUtil.generateToken(userDetails);
        
        // Esperar un momento para que el token expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Resetear expiración normal
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtExpirationMs", 3600000L);
        
        // Verificar que se lanza la excepción esperada
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            jwtTokenUtil.validateToken(token, userDetails);
        });
    }

    @Test
    void testInvalidToken() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();

        String invalidToken = "invalid.token.here";
        
        assertThrows(Exception.class, () -> {
            jwtTokenUtil.validateToken(invalidToken, userDetails);
        });
    }
}
