package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos() {
        List<Producto> productos = Arrays.asList(
                Producto.builder().id(1L).nombre("Producto 1").precio(BigDecimal.valueOf(10.0)).build(),
                Producto.builder().id(2L).nombre("Producto 2").precio(BigDecimal.valueOf(20.0)).build()
        );

        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(productoRepository).findAll();
    }

    @Test
    void testObtenerPorId() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto 1")
                .precio(BigDecimal.valueOf(10.0))
                .build();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Producto 1", resultado.get().getNombre());
        verify(productoRepository).findById(1L);
    }

    @Test
    void testGuardar() {
        Producto producto = Producto.builder()
                .nombre("Nuevo Producto")
                .precio(BigDecimal.valueOf(15.0))
                .build();

        Producto productoGuardado = Producto.builder()
                .id(1L)
                .nombre("Nuevo Producto")
                .precio(BigDecimal.valueOf(15.0))
                .build();

        when(productoRepository.save(producto)).thenReturn(productoGuardado);

        Producto resultado = productoService.guardar(producto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Nuevo Producto", resultado.getNombre());
        verify(productoRepository).save(producto);
    }

    @Test
    void testEliminar() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.eliminar(1L);

        verify(productoRepository).deleteById(1L);
    }
}
