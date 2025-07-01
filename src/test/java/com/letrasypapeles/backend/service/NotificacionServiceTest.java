package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Notificacion;
import com.letrasypapeles.backend.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodas() {
        List<Notificacion> notificaciones = Arrays.asList(
                Notificacion.builder().id(1L).mensaje("Notificación 1").fecha(LocalDateTime.now()).build(),
                Notificacion.builder().id(2L).mensaje("Notificación 2").fecha(LocalDateTime.now()).build()
        );

        when(notificacionRepository.findAll()).thenReturn(notificaciones);

        List<Notificacion> resultado = notificacionService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(notificacionRepository).findAll();
    }

    @Test
    void testObtenerPorId() {
        Notificacion notificacion = Notificacion.builder()
                .id(1L)
                .mensaje("Notificación 1")
                .fecha(LocalDateTime.now())
                .build();

        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        Optional<Notificacion> resultado = notificacionService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Notificación 1", resultado.get().getMensaje());
        verify(notificacionRepository).findById(1L);
    }

    @Test
    void testGuardar() {
        Notificacion notificacion = Notificacion.builder()
                .mensaje("Nueva notificación")
                .fecha(LocalDateTime.now())
                .build();

        Notificacion notificacionGuardada = Notificacion.builder()
                .id(1L)
                .mensaje("Nueva notificación")
                .fecha(LocalDateTime.now())
                .build();

        when(notificacionRepository.save(notificacion)).thenReturn(notificacionGuardada);

        Notificacion resultado = notificacionService.guardar(notificacion);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Nueva notificación", resultado.getMensaje());
        verify(notificacionRepository).save(notificacion);
    }

    @Test
    void testEliminar() {
        doNothing().when(notificacionRepository).deleteById(1L);

        notificacionService.eliminar(1L);

        verify(notificacionRepository).deleteById(1L);
    }

    @Test
    void testObtenerPorClienteId() {
        List<Notificacion> notificaciones = Arrays.asList(
                Notificacion.builder().id(1L).mensaje("Notificación 1").fecha(LocalDateTime.now()).build(),
                Notificacion.builder().id(2L).mensaje("Notificación 2").fecha(LocalDateTime.now()).build()
        );

        when(notificacionRepository.findByClienteId(1L)).thenReturn(notificaciones);

        List<Notificacion> resultado = notificacionService.obtenerPorClienteId(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(notificacionRepository).findByClienteId(1L);
    }

    @Test
    void testObtenerPorFechaEntre() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fin = LocalDateTime.now();
        
        List<Notificacion> notificaciones = Arrays.asList(
                Notificacion.builder().id(1L).mensaje("Notificación 1").fecha(LocalDateTime.now()).build(),
                Notificacion.builder().id(2L).mensaje("Notificación 2").fecha(LocalDateTime.now()).build()
        );

        when(notificacionRepository.findByFechaBetween(inicio, fin)).thenReturn(notificaciones);

        List<Notificacion> resultado = notificacionService.obtenerPorFechaEntre(inicio, fin);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(notificacionRepository).findByFechaBetween(inicio, fin);
    }
}
