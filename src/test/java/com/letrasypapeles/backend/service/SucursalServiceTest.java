package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Sucursal;
import com.letrasypapeles.backend.repository.SucursalRepository;
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

class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodas() {
        List<Sucursal> sucursales = Arrays.asList(
                Sucursal.builder().id(1L).nombre("Sucursal 1").build(),
                Sucursal.builder().id(2L).nombre("Sucursal 2").build()
        );

        when(sucursalRepository.findAll()).thenReturn(sucursales);

        List<Sucursal> resultado = sucursalService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(sucursalRepository).findAll();
    }

    @Test
    void testObtenerPorId() {
        Sucursal sucursal = Sucursal.builder().id(1L).nombre("Sucursal 1").build();

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));

        Optional<Sucursal> resultado = sucursalService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Sucursal 1", resultado.get().getNombre());
        verify(sucursalRepository).findById(1L);
    }

    @Test
    void testGuardar() {
        Sucursal sucursal = Sucursal.builder().nombre("Nueva Sucursal").build();
        Sucursal sucursalGuardada = Sucursal.builder().id(1L).nombre("Nueva Sucursal").build();

        when(sucursalRepository.save(sucursal)).thenReturn(sucursalGuardada);

        Sucursal resultado = sucursalService.guardar(sucursal);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Nueva Sucursal", resultado.getNombre());
        verify(sucursalRepository).save(sucursal);
    }

    @Test
    void testEliminar() {
        doNothing().when(sucursalRepository).deleteById(1L);

        sucursalService.eliminar(1L);

        verify(sucursalRepository).deleteById(1L);
    }
}
