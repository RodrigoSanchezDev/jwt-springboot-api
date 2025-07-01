package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.service.InventarioService;
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

class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController inventarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodosInventarios() {
        List<Inventario> inventarios = Arrays.asList(
                Inventario.builder().id(1L).cantidad(10).build(),
                Inventario.builder().id(2L).cantidad(20).build()
        );

        when(inventarioService.obtenerTodos()).thenReturn(inventarios);

        ResponseEntity<List<Inventario>> response = inventarioController.obtenerTodos();

        assertEquals(200, response.getStatusCode().value());
        List<Inventario> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
    }

    @Test
    void testObtenerInventarioPorId() {
        Inventario inventario = Inventario.builder().id(1L).cantidad(10).build();

        when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.of(inventario));

        ResponseEntity<Inventario> response = inventarioController.obtenerPorId(1L);

        assertEquals(200, response.getStatusCode().value());
        Inventario responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(10, responseBody.getCantidad());
    }

    @Test
    void testCrearInventario() {
        Inventario inventario = Inventario.builder().cantidad(15).build();
        Inventario inventarioGuardado = Inventario.builder().id(1L).cantidad(15).build();

        when(inventarioService.guardar(inventario)).thenReturn(inventarioGuardado);

        ResponseEntity<Inventario> response = inventarioController.crearInventario(inventario);

        assertEquals(200, response.getStatusCode().value());
        Inventario responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.getId());
        assertEquals(15, responseBody.getCantidad());
    }

    @Test
    void testActualizarInventario() {
        Inventario inventarioExistente = Inventario.builder().id(1L).cantidad(10).build();
        Inventario inventarioActualizado = Inventario.builder().id(1L).cantidad(20).build();

        when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.of(inventarioExistente));
        when(inventarioService.guardar(inventarioActualizado)).thenReturn(inventarioActualizado);

        ResponseEntity<Inventario> response = inventarioController.actualizarInventario(1L, inventarioActualizado);

        assertEquals(200, response.getStatusCode().value());
        Inventario responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(20, responseBody.getCantidad());
    }

    @Test
    void testEliminarInventario() {
        Inventario inventarioExistente = Inventario.builder().id(1L).cantidad(10).build();

        when(inventarioService.obtenerPorId(1L)).thenReturn(Optional.of(inventarioExistente));

        ResponseEntity<Void> response = inventarioController.eliminarInventario(1L);

        assertEquals(200, response.getStatusCode().value());
    }
}
