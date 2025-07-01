package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodosRoles() {
        List<Role> roles = Arrays.asList(
                Role.builder().nombre("CLIENTE").build(),
                Role.builder().nombre("ADMIN").build()
        );

        when(roleService.obtenerTodos()).thenReturn(roles);

        ResponseEntity<List<Role>> response = roleController.obtenerTodos();

        assertEquals(200, response.getStatusCode().value());
        List<Role> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
    }

    @Test
    void testObtenerRolPorNombre() {
        Role role = Role.builder().nombre("CLIENTE").build();

        when(roleService.obtenerPorNombre("CLIENTE")).thenReturn(Optional.of(role));

        ResponseEntity<Role> response = roleController.obtenerPorNombre("CLIENTE");

        assertEquals(200, response.getStatusCode().value());
        Role responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("CLIENTE", responseBody.getNombre());
    }

    @Test
    void testObtenerRolPorNombreNoEncontrado() {
        when(roleService.obtenerPorNombre("NO_EXISTE")).thenReturn(Optional.empty());

        ResponseEntity<Role> response = roleController.obtenerPorNombre("NO_EXISTE");

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testCrearRole() {
        Role role = Role.builder().nombre("NUEVO_ROL").build();

        when(roleService.guardar(role)).thenReturn(role);

        ResponseEntity<Role> response = roleController.crearRole(role);

        assertEquals(200, response.getStatusCode().value());
        Role responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("NUEVO_ROL", responseBody.getNombre());
    }

    @Test
    void testEliminarRole() {
        Role roleExistente = Role.builder().nombre("ROL_A_ELIMINAR").build();

        when(roleService.obtenerPorNombre("ROL_A_ELIMINAR")).thenReturn(Optional.of(roleExistente));
        doNothing().when(roleService).eliminar("ROL_A_ELIMINAR");

        ResponseEntity<Void> response = roleController.eliminarRole("ROL_A_ELIMINAR");

        assertEquals(200, response.getStatusCode().value());
        verify(roleService).eliminar("ROL_A_ELIMINAR");
    }

    @Test
    void testEliminarRoleNoExistente() {
        when(roleService.obtenerPorNombre("NO_EXISTE")).thenReturn(Optional.empty());

        ResponseEntity<Void> response = roleController.eliminarRole("NO_EXISTE");

        assertEquals(404, response.getStatusCode().value());
        verify(roleService, never()).eliminar("NO_EXISTE");
    }
}
