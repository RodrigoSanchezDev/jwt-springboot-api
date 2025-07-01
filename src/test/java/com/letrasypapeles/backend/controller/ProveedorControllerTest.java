package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.service.ProveedorService;
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
import static org.mockito.Mockito.when;

class ProveedorControllerTest {

    @Mock
    private ProveedorService proveedorService;

    @InjectMocks
    private ProveedorController proveedorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodosProveedores() {
        List<Proveedor> proveedores = Arrays.asList(
                Proveedor.builder().id(1L).nombre("Proveedor 1").build(),
                Proveedor.builder().id(2L).nombre("Proveedor 2").build()
        );

        when(proveedorService.obtenerTodos()).thenReturn(proveedores);

        ResponseEntity<List<Proveedor>> response = proveedorController.obtenerTodos();

        assertEquals(200, response.getStatusCode().value());
        List<Proveedor> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
    }

    @Test
    void testObtenerProveedorPorId() {
        Proveedor proveedor = Proveedor.builder().id(1L).nombre("Proveedor 1").build();

        when(proveedorService.obtenerPorId(1L)).thenReturn(Optional.of(proveedor));

        ResponseEntity<Proveedor> response = proveedorController.obtenerPorId(1L);

        assertEquals(200, response.getStatusCode().value());
        Proveedor responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Proveedor 1", responseBody.getNombre());
    }

    @Test
    void testCrearProveedor() {
        Proveedor proveedor = Proveedor.builder().nombre("Proveedor Nuevo").build();
        Proveedor proveedorGuardado = Proveedor.builder().id(1L).nombre("Proveedor Nuevo").build();

        when(proveedorService.guardar(proveedor)).thenReturn(proveedorGuardado);

        ResponseEntity<Proveedor> response = proveedorController.crearProveedor(proveedor);

        assertEquals(200, response.getStatusCode().value());
        Proveedor responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.getId());
        assertEquals("Proveedor Nuevo", responseBody.getNombre());
    }

    @Test
    void testActualizarProveedor() {
        Proveedor proveedorExistente = Proveedor.builder().id(1L).nombre("Proveedor Existente").build();
        Proveedor proveedorActualizado = Proveedor.builder().id(1L).nombre("Proveedor Actualizado").build();

        when(proveedorService.obtenerPorId(1L)).thenReturn(Optional.of(proveedorExistente));
        when(proveedorService.guardar(proveedorActualizado)).thenReturn(proveedorActualizado);

        ResponseEntity<Proveedor> response = proveedorController.actualizarProveedor(1L, proveedorActualizado);

        assertEquals(200, response.getStatusCode().value());
        Proveedor responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Proveedor Actualizado", responseBody.getNombre());
    }

    @Test
    void testEliminarProveedor() {
        Proveedor proveedorExistente = Proveedor.builder().id(1L).nombre("Proveedor Existente").build();

        when(proveedorService.obtenerPorId(1L)).thenReturn(Optional.of(proveedorExistente));

        ResponseEntity<Void> response = proveedorController.eliminarProveedor(1L);

        assertEquals(200, response.getStatusCode().value());
    }
}
