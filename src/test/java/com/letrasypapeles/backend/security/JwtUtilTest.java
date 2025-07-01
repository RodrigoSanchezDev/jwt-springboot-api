package com.letrasypapeles.backend.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Configurar valores de prueba
        ReflectionTestUtils.setField(jwtUtil, "secret", "testSecretKeyForJwtTokens123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationInMs", 86400000L); // 24 horas en ms

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("CLIENTE"));
        authorities.add(new SimpleGrantedAuthority("ADMIN"));

        userDetails = new User("test@example.com", "password", authorities);
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.startsWith("eyJ")); // JWT tokens start with eyJ
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken(userDetails);

        String username = jwtUtil.extractUsername(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void testExtractExpiration() {
        String token = jwtUtil.generateToken(userDetails);

        Date expiration = jwtUtil.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void testValidateToken_Success() {
        String token = jwtUtil.generateToken(userDetails);

        boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_WrongUser() {
        String token = jwtUtil.generateToken(userDetails);

        UserDetails differentUser = new User("different@example.com", "password", 
                List.of(new SimpleGrantedAuthority("CLIENTE")));

        boolean isValid = jwtUtil.validateToken(token, differentUser);

        assertFalse(isValid);
    }

    @Test
    void testExtractAuthorities() {
        String token = jwtUtil.generateToken(userDetails);

        List<String> authorities = jwtUtil.extractAuthorities(token);

        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains("CLIENTE"));
        assertTrue(authorities.contains("ADMIN"));
    }

    @Test
    void testIsTokenExpired_NotExpired() {
        String token = jwtUtil.generateToken(userDetails);

        // Test reflection para acceder al método privado isTokenExpired
        Date expiration = jwtUtil.extractExpiration(token);
        boolean isExpired = expiration.before(new Date());

        assertFalse(isExpired);
    }

    @Test
    void testGenerateTokenWithSingleRole() {
        UserDetails singleRoleUser = new User("single@example.com", "password", 
                List.of(new SimpleGrantedAuthority("EMPLOYEE")));

        String token = jwtUtil.generateToken(singleRoleUser);
        List<String> authorities = jwtUtil.extractAuthorities(token);

        assertNotNull(token);
        assertEquals(1, authorities.size());
        assertEquals("EMPLOYEE", authorities.get(0));
    }

    @Test
    void testGenerateTokenWithNoRoles() {
        UserDetails noRoleUser = new User("norole@example.com", "password", List.of());

        String token = jwtUtil.generateToken(noRoleUser);
        List<String> authorities = jwtUtil.extractAuthorities(token);

        assertNotNull(token);
        assertEquals(0, authorities.size());
    }
    
    @Test
    void testValidateTokenWithExpiredToken() {
        // Generar token que expire inmediatamente
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationInMs", 1L); // 1 ms
        
        String token = jwtUtil.generateToken(userDetails);
        
        // Esperar a que expire
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Restaurar tiempo de expiración normal
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationInMs", 86400000L);
        
        boolean isValid = jwtUtil.validateToken(token, userDetails);
        assertFalse(isValid);
    }
    
    @Test
    void testExtractUsernameWithInvalidToken() {
        String invalidToken = "invalid.jwt.token";
        
        assertThrows(Exception.class, () -> {
            jwtUtil.extractUsername(invalidToken);
        });
    }
    
    @Test
    void testExtractAuthoritiesWithInvalidToken() {
        String invalidToken = "invalid.jwt.token";
        
        assertThrows(Exception.class, () -> {
            jwtUtil.extractAuthorities(invalidToken);
        });
    }
    
    @Test
    void testValidateTokenWithNullToken() {
        boolean isValid = jwtUtil.validateToken(null, userDetails);
        assertFalse(isValid);
    }
    
    @Test
    void testValidateTokenWithEmptyToken() {
        boolean isValid = jwtUtil.validateToken("", userDetails);
        assertFalse(isValid);
    }
    
    @Test
    void testExtractUsernameWithNullToken() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractUsername(null);
        });
    }
    
    @Test
    void testExtractExpirationWithInvalidToken() {
        String invalidToken = "invalid.jwt.token";
        
        assertThrows(Exception.class, () -> {
            jwtUtil.extractExpiration(invalidToken);
        });
    }
}
