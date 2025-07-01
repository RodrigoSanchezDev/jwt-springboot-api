package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.repository.ProveedorRepository;
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

class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos() {
        List<Proveedor> proveedores = Arrays.asList(
                Proveedor.builder().id(1L).nombre("Proveedor 1").contacto("contacto1@email.com").build(),
                Proveedor.builder().id(2L).nombre("Proveedor 2").contacto("contacto2@email.com").build()
        );

        when(proveedorRepository.findAll()).thenReturn(proveedores);

        List<Proveedor> resultado = proveedorService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(proveedorRepository).findAll();
    }

    @Test
    void testObtenerPorId() {
        Proveedor proveedor = Proveedor.builder()
                .id(1L)
                .nombre("Proveedor 1")
                .contacto("contacto1@email.com")
                .build();

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));

        Optional<Proveedor> resultado = proveedorService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Proveedor 1", resultado.get().getNombre());
        verify(proveedorRepository).findById(1L);
    }

    @Test
    void testGuardar() {
        Proveedor proveedor = Proveedor.builder()
                .nombre("Nuevo Proveedor")
                .contacto("nuevo@email.com")
                .build();

        Proveedor proveedorGuardado = Proveedor.builder()
                .id(1L)
                .nombre("Nuevo Proveedor")
                .contacto("nuevo@email.com")
                .build();

        when(proveedorRepository.save(proveedor)).thenReturn(proveedorGuardado);

        Proveedor resultado = proveedorService.guardar(proveedor);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Nuevo Proveedor", resultado.getNombre());
        verify(proveedorRepository).save(proveedor);
    }

    @Test
    void testEliminar() {
        doNothing().when(proveedorRepository).deleteById(1L);

        proveedorService.eliminar(1L);

        verify(proveedorRepository).deleteById(1L);
    }
}
