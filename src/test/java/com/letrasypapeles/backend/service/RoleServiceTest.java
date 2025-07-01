package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos() {
        List<Role> roles = Arrays.asList(
                Role.builder().nombre("CLIENTE").build(),
                Role.builder().nombre("ADMIN").build()
        );

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> resultado = roleService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(roleRepository).findAll();
    }

    @Test
    void testObtenerPorNombre() {
        Role role = Role.builder().nombre("CLIENTE").build();

        when(roleRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(role));

        Optional<Role> resultado = roleService.obtenerPorNombre("CLIENTE");

        assertTrue(resultado.isPresent());
        assertEquals("CLIENTE", resultado.get().getNombre());
        verify(roleRepository).findByNombre("CLIENTE");
    }

    @Test
    void testGuardar() {
        Role role = Role.builder().nombre("NUEVO_ROL").build();

        when(roleRepository.save(role)).thenReturn(role);

        Role resultado = roleService.guardar(role);

        assertNotNull(resultado);
        assertEquals("NUEVO_ROL", resultado.getNombre());
        verify(roleRepository).save(role);
    }

    @Test
    void testEliminar() {
        doNothing().when(roleRepository).deleteById("ROLE_A_ELIMINAR");

        roleService.eliminar("ROLE_A_ELIMINAR");

        verify(roleRepository).deleteById("ROLE_A_ELIMINAR");
    }
}
