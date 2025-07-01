package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.service.ProductoService;
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

class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodosProductos() {
        List<Producto> productos = Arrays.asList(
                Producto.builder().id(1L).nombre("Producto 1").build(),
                Producto.builder().id(2L).nombre("Producto 2").build()
        );

        when(productoService.obtenerTodos()).thenReturn(productos);

        ResponseEntity<List<Producto>> response = productoController.obtenerTodos();

        assertEquals(200, response.getStatusCode().value());
        List<Producto> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
    }

    @Test
    void testObtenerProductoPorId() {
        Producto producto = Producto.builder().id(1L).nombre("Producto 1").build();

        when(productoService.obtenerPorId(1L)).thenReturn(Optional.of(producto));

        ResponseEntity<Producto> response = productoController.obtenerPorId(1L);

        assertEquals(200, response.getStatusCode().value());
        Producto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Producto 1", responseBody.getNombre());
    }

    @Test
    void testCrearProducto() {
        Producto producto = Producto.builder().nombre("Producto Nuevo").build();
        Producto productoGuardado = Producto.builder().id(1L).nombre("Producto Nuevo").build();

        when(productoService.guardar(producto)).thenReturn(productoGuardado);

        ResponseEntity<Producto> response = productoController.crearProducto(producto);

        assertEquals(200, response.getStatusCode().value());
        Producto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.getId());
        assertEquals("Producto Nuevo", responseBody.getNombre());
    }

    @Test
    void testActualizarProducto() {
        Producto productoExistente = Producto.builder().id(1L).nombre("Producto Existente").build();
        Producto productoActualizado = Producto.builder().id(1L).nombre("Producto Actualizado").build();

        when(productoService.obtenerPorId(1L)).thenReturn(Optional.of(productoExistente));
        when(productoService.guardar(productoActualizado)).thenReturn(productoActualizado);

        ResponseEntity<Producto> response = productoController.actualizarProducto(1L, productoActualizado);

        assertEquals(200, response.getStatusCode().value());
        Producto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Producto Actualizado", responseBody.getNombre());
    }

    @Test
    void testEliminarProducto() {
        Producto productoExistente = Producto.builder().id(1L).nombre("Producto Existente").build();

        when(productoService.obtenerPorId(1L)).thenReturn(Optional.of(productoExistente));

        ResponseEntity<Void> response = productoController.eliminarProducto(1L);

        assertEquals(200, response.getStatusCode().value());
    }
}
