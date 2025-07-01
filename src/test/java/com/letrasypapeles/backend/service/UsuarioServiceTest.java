package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Cliente cliente;
    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .nombre("CLIENTE")
                .build();

        cliente = Cliente.builder()
                .id(1L)
                .email("test@example.com")
                .contraseña("$2a$10$hashedPassword")
                .nombre("Test User")
                .roles(Set.of(role))
                .build();
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(clienteRepository.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));

        UserDetails userDetails = usuarioService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("$2a$10$hashedPassword", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("CLIENTE")));
        verify(clienteRepository).findByEmail("test@example.com");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(clienteRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            usuarioService.loadUserByUsername("notfound@example.com");
        });

        verify(clienteRepository).findByEmail("notfound@example.com");
    }

    @Test
    void testLoadUserByUsername_MultipleRoles() {
        Role adminRole = Role.builder()
                .nombre("ADMIN")
                .build();

        cliente.setRoles(Set.of(role, adminRole));

        when(clienteRepository.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));

        UserDetails userDetails = usuarioService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("CLIENTE")));
        assertTrue(userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ADMIN")));
        verify(clienteRepository).findByEmail("test@example.com");
    }
}
