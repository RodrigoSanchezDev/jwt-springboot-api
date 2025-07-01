package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Sucursal;
import com.letrasypapeles.backend.service.SucursalService;
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

class SucursalControllerTest {

    @Mock
    private SucursalService sucursalService;

    @InjectMocks
    private SucursalController sucursalController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodasSucursales() {
        List<Sucursal> sucursales = Arrays.asList(
                Sucursal.builder().id(1L).nombre("Sucursal 1").direccion("Dirección 1").build(),
                Sucursal.builder().id(2L).nombre("Sucursal 2").direccion("Dirección 2").build()
        );

        when(sucursalService.obtenerTodas()).thenReturn(sucursales);

        ResponseEntity<List<Sucursal>> response = sucursalController.obtenerTodas();

        assertEquals(200, response.getStatusCode().value());
        List<Sucursal> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
    }

    @Test
    void testObtenerSucursalPorId() {
        Sucursal sucursal = Sucursal.builder().id(1L).nombre("Sucursal 1").direccion("Dirección 1").build();

        when(sucursalService.obtenerPorId(1L)).thenReturn(Optional.of(sucursal));

        ResponseEntity<Sucursal> response = sucursalController.obtenerPorId(1L);

        assertEquals(200, response.getStatusCode().value());
        Sucursal responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Sucursal 1", responseBody.getNombre());
    }

    @Test
    void testObtenerSucursalPorIdNoEncontrada() {
        when(sucursalService.obtenerPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Sucursal> response = sucursalController.obtenerPorId(1L);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testCrearSucursal() {
        Sucursal sucursal = Sucursal.builder().nombre("Nueva Sucursal").direccion("Nueva Dirección").build();
        Sucursal sucursalGuardada = Sucursal.builder().id(1L).nombre("Nueva Sucursal").direccion("Nueva Dirección").build();

        when(sucursalService.guardar(sucursal)).thenReturn(sucursalGuardada);

        ResponseEntity<Sucursal> response = sucursalController.crearSucursal(sucursal);

        assertEquals(200, response.getStatusCode().value());
        Sucursal responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.getId());
    }

    @Test
    void testActualizarSucursal() {
        Sucursal sucursalExistente = Sucursal.builder().id(1L).nombre("Sucursal 1").direccion("Dirección 1").build();
        Sucursal sucursalActualizada = Sucursal.builder().id(1L).nombre("Sucursal Actualizada").direccion("Dirección Actualizada").build();

        when(sucursalService.obtenerPorId(1L)).thenReturn(Optional.of(sucursalExistente));
        when(sucursalService.guardar(sucursalActualizada)).thenReturn(sucursalActualizada);

        ResponseEntity<Sucursal> response = sucursalController.actualizarSucursal(1L, sucursalActualizada);

        assertEquals(200, response.getStatusCode().value());
        Sucursal responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Sucursal Actualizada", responseBody.getNombre());
    }

    @Test
    void testEliminarSucursal() {
        Sucursal sucursalExistente = Sucursal.builder().id(1L).nombre("Sucursal 1").build();

        when(sucursalService.obtenerPorId(1L)).thenReturn(Optional.of(sucursalExistente));
        doNothing().when(sucursalService).eliminar(1L);

        ResponseEntity<Void> response = sucursalController.eliminarSucursal(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(sucursalService).eliminar(1L);
    }
}
