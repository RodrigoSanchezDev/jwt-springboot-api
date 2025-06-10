package com.letrasypapeles.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    // La clave secreta la leemos desde application.properties
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Expiración en milisegundos
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // Generar token a partir de UserDetails 
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extraer el email de un token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Extraer cualquier claim usando una función
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Verificar si el token está expirado
    public boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    // Validar token contra los datos del usuario
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return ( username.equals(userDetails.getUsername()) && !isTokenExpired(token) );
    }

    // Método interno: parsear todos los claims
    private Claims parseAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (JwtException ex) {
            // cualquier otro error de parseo / validación
            throw new RuntimeException("JWT inválido o expirado");
        }
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
