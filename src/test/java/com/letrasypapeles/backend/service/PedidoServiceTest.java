package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.repository.PedidoRepository;
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

class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos() {
        List<Pedido> pedidos = Arrays.asList(
                Pedido.builder().id(1L).estado("Pendiente").fecha(LocalDateTime.now()).build(),
                Pedido.builder().id(2L).estado("Entregado").fecha(LocalDateTime.now()).build()
        );

        when(pedidoRepository.findAll()).thenReturn(pedidos);

        List<Pedido> resultado = pedidoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pedidoRepository).findAll();
    }

    @Test
    void testObtenerPorId() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .estado("Pendiente")
                .fecha(LocalDateTime.now())
                .build();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Optional<Pedido> resultado = pedidoService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Pendiente", resultado.get().getEstado());
        verify(pedidoRepository).findById(1L);
    }

    @Test
    void testGuardar() {
        Pedido pedido = Pedido.builder()
                .estado("Pendiente")
                .fecha(LocalDateTime.now())
                .build();

        Pedido pedidoGuardado = Pedido.builder()
                .id(1L)
                .estado("Pendiente")
                .fecha(LocalDateTime.now())
                .build();

        when(pedidoRepository.save(pedido)).thenReturn(pedidoGuardado);

        Pedido resultado = pedidoService.guardar(pedido);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pendiente", resultado.getEstado());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void testEliminar() {
        doNothing().when(pedidoRepository).deleteById(1L);

        pedidoService.eliminar(1L);

        verify(pedidoRepository).deleteById(1L);
    }

    @Test
    void testObtenerPorClienteId() {
        List<Pedido> pedidos = Arrays.asList(
                Pedido.builder().id(1L).estado("Pendiente").fecha(LocalDateTime.now()).build(),
                Pedido.builder().id(2L).estado("Entregado").fecha(LocalDateTime.now()).build()
        );

        when(pedidoRepository.findByClienteId(1L)).thenReturn(pedidos);

        List<Pedido> resultado = pedidoService.obtenerPorClienteId(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pedidoRepository).findByClienteId(1L);
    }

    @Test
    void testObtenerPorEstado() {
        List<Pedido> pedidos = Arrays.asList(
                Pedido.builder().id(1L).estado("Pendiente").fecha(LocalDateTime.now()).build(),
                Pedido.builder().id(3L).estado("Pendiente").fecha(LocalDateTime.now()).build()
        );

        when(pedidoRepository.findByEstado("Pendiente")).thenReturn(pedidos);

        List<Pedido> resultado = pedidoService.obtenerPorEstado("Pendiente");

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pedidoRepository).findByEstado("Pendiente");
    }
}
