package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.entity.Sucursal;
import com.letrasypapeles.backend.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario inventario;
    private Producto producto;
    private Sucursal sucursal;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .build();

        sucursal = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Test")
                .build();

        inventario = Inventario.builder()
                .id(1L)
                .cantidad(100)
                .umbral(10)
                .producto(producto)
                .sucursal(sucursal)
                .stockActual(100)
                .umbralMinimo(10)
                .build();
    }

    @Test
    void testObtenerTodos() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findAll()).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(inventario.getId(), resultado.get(0).getId());
        verify(inventarioRepository).findAll();
    }

    @Test
    void testObtenerPorId() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        Optional<Inventario> resultado = inventarioService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(inventario.getId(), resultado.get().getId());
        verify(inventarioRepository).findById(1L);
    }

    @Test
    void testObtenerPorIdNoEncontrado() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Inventario> resultado = inventarioService.obtenerPorId(1L);

        assertFalse(resultado.isPresent());
        verify(inventarioRepository).findById(1L);
    }

    @Test
    void testGuardar() {
        Inventario nuevoInventario = Inventario.builder()
                .cantidad(50)
                .umbral(5)
                .producto(producto)
                .sucursal(sucursal)
                .build();

        Inventario inventarioGuardado = Inventario.builder()
                .id(2L)
                .cantidad(50)
                .umbral(5)
                .producto(producto)
                .sucursal(sucursal)
                .build();

        when(inventarioRepository.save(nuevoInventario)).thenReturn(inventarioGuardado);

        Inventario resultado = inventarioService.guardar(nuevoInventario);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals(50, resultado.getCantidad());
        verify(inventarioRepository).save(nuevoInventario);
    }

    @Test
    void testEliminar() {
        doNothing().when(inventarioRepository).deleteById(1L);

        inventarioService.eliminar(1L);

        verify(inventarioRepository).deleteById(1L);
    }

    @Test
    void testObtenerPorProductoId() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findByProductoId(1L)).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.obtenerPorProductoId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(inventario.getId(), resultado.get(0).getId());
        verify(inventarioRepository).findByProductoId(1L);
    }

    @Test
    void testObtenerPorSucursalId() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findBySucursalId(1L)).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.obtenerPorSucursalId(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(inventario.getId(), resultado.get(0).getId());
        verify(inventarioRepository).findBySucursalId(1L);
    }

    @Test
    void testObtenerInventarioBajoUmbral() {
        List<Inventario> inventarios = Arrays.asList(inventario);
        when(inventarioRepository.findByCantidadLessThan(20)).thenReturn(inventarios);

        List<Inventario> resultado = inventarioService.obtenerInventarioBajoUmbral(20);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(inventario.getId(), resultado.get(0).getId());
        verify(inventarioRepository).findByCantidadLessThan(20);
    }
}
