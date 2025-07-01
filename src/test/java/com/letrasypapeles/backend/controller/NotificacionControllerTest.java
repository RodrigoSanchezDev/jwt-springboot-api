package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Notificacion;
import com.letrasypapeles.backend.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.any;

class NotificacionControllerTest {

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private NotificacionController notificacionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodasNotificaciones() {
        List<Notificacion> notificaciones = Arrays.asList(
                Notificacion.builder()
                        .id(1L)
                        .mensaje("Notificación 1")
                        .fecha(LocalDateTime.now())
                        .build(),
                Notificacion.builder()
                        .id(2L)
                        .mensaje("Notificación 2")
                        .fecha(LocalDateTime.now())
                        .build()
        );

        when(notificacionService.obtenerTodas()).thenReturn(notificaciones);

        ResponseEntity<List<Notificacion>> response = notificacionController.obtenerTodas();

        assertEquals(200, response.getStatusCode().value());
        List<Notificacion> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
    }

    @Test
    void testObtenerNotificacionPorId() {
        Notificacion notificacion = Notificacion.builder()
                .id(1L)
                .mensaje("Notificación 1")
                .fecha(LocalDateTime.now())
                .build();

        when(notificacionService.obtenerPorId(1L)).thenReturn(Optional.of(notificacion));

        ResponseEntity<Notificacion> response = notificacionController.obtenerPorId(1L);

        assertEquals(200, response.getStatusCode().value());
        Notificacion responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Notificación 1", responseBody.getMensaje());
    }

    @Test
    void testCrearNotificacion() {
        Notificacion notificacion = Notificacion.builder()
                .mensaje("Nueva notificación")
                .fecha(LocalDateTime.now())
                .build();
        
        Notificacion notificacionGuardada = Notificacion.builder()
                .id(1L)
                .mensaje("Nueva notificación")
                .fecha(LocalDateTime.now())
                .build();

        when(notificacionService.guardar(notificacion)).thenReturn(notificacionGuardada);

        ResponseEntity<Notificacion> response = notificacionController.crearNotificacion(notificacion);

        assertEquals(200, response.getStatusCode().value());
        Notificacion responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.getId());
        assertEquals("Nueva notificación", responseBody.getMensaje());
    }

    @Test
    void testObtenerPorClienteId() {
        List<Notificacion> notificaciones = Arrays.asList(
                Notificacion.builder().id(1L).mensaje("Notificación 1").fecha(LocalDateTime.now()).build(),
                Notificacion.builder().id(2L).mensaje("Notificación 2").fecha(LocalDateTime.now()).build()
        );

        when(notificacionService.obtenerPorClienteId(1L)).thenReturn(notificaciones);

        ResponseEntity<List<Notificacion>> response = notificacionController.obtenerPorClienteId(1L);

        assertEquals(200, response.getStatusCode().value());
        List<Notificacion> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
    }

    @Test
    void testEliminarNotificacion() {
        Notificacion notificacion = Notificacion.builder()
                .id(1L)
                .mensaje("Notificación")
                .build();

        when(notificacionService.obtenerPorId(1L)).thenReturn(Optional.of(notificacion));
        doNothing().when(notificacionService).eliminar(1L);

        ResponseEntity<Void> response = notificacionController.eliminarNotificacion(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(notificacionService).eliminar(1L);
    }

    @Test
    void testObtenerNotificacionPorIdNotFound() {
        when(notificacionService.obtenerPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<Notificacion> response = notificacionController.obtenerPorId(999L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(notificacionService).obtenerPorId(999L);
    }

    @Test
    void testEliminarNotificacionNotFound() {
        when(notificacionService.obtenerPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = notificacionController.eliminarNotificacion(999L);

        assertEquals(404, response.getStatusCode().value());
        verify(notificacionService).obtenerPorId(999L);
        verify(notificacionService, never()).eliminar(999L);
    }

    @Test
    void testObtenerPorClienteIdEmptyList() {
        when(notificacionService.obtenerPorClienteId(999L)).thenReturn(java.util.Collections.emptyList());

        ResponseEntity<List<Notificacion>> response = notificacionController.obtenerPorClienteId(999L);

        assertEquals(200, response.getStatusCode().value());
        List<Notificacion> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isEmpty());
        verify(notificacionService).obtenerPorClienteId(999L);
    }

    @Test
    void testObtenerTodasEmptyList() {
        when(notificacionService.obtenerTodas()).thenReturn(java.util.Collections.emptyList());

        ResponseEntity<List<Notificacion>> response = notificacionController.obtenerTodas();

        assertEquals(200, response.getStatusCode().value());
        List<Notificacion> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isEmpty());
        verify(notificacionService).obtenerTodas();
    }

    @Test
    void testCrearNotificacionSetsFecha() {
        Notificacion notificacion = Notificacion.builder()
                .mensaje("Notificación sin fecha")
                .build();
        
        // Verificar que la fecha se asigna automáticamente
        LocalDateTime beforeCreate = LocalDateTime.now().minusSeconds(1);
        
        Notificacion notificacionGuardada = Notificacion.builder()
                .id(1L)
                .mensaje("Notificación sin fecha")
                .fecha(LocalDateTime.now())
                .build();

        when(notificacionService.guardar(any(Notificacion.class))).thenReturn(notificacionGuardada);

        ResponseEntity<Notificacion> response = notificacionController.crearNotificacion(notificacion);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        
        // Verificar que se llamó con una notificación que tiene fecha
        verify(notificacionService).guardar(argThat(n -> 
            n.getFecha() != null && n.getFecha().isAfter(beforeCreate)
        ));
    }
}
