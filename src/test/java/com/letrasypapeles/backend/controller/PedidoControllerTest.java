package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Pedido;
import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido pedido;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@email.com")
                .build();

        pedido = Pedido.builder()
                .id(1L)
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .cliente(cliente)
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void obtenerTodos_DeberiaRetornarListaDePedidos() throws Exception {
        List<Pedido> pedidos = Arrays.asList(pedido);
        when(pedidoService.obtenerTodos()).thenReturn(pedidos);

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));

        verify(pedidoService, times(1)).obtenerTodos();
    }

    @Test
    @WithMockUser(roles = "USER")
    void obtenerPorId_CuandoExiste_DeberiaRetornarPedido() throws Exception {
        when(pedidoService.obtenerPorId(1L)).thenReturn(Optional.of(pedido));

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        verify(pedidoService, times(1)).obtenerPorId(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void obtenerPorId_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        when(pedidoService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pedidos/999"))
                .andExpect(status().isNotFound());

        verify(pedidoService, times(1)).obtenerPorId(999L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void obtenerPorClienteId_DeberiaRetornarPedidosDelCliente() throws Exception {
        List<Pedido> pedidos = Arrays.asList(pedido);
        when(pedidoService.obtenerPorClienteId(1L)).thenReturn(pedidos);

        mockMvc.perform(get("/api/pedidos/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"));

        verify(pedidoService, times(1)).obtenerPorClienteId(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void crearPedido_DeberiaCrearYRetornarPedido() throws Exception {
        when(pedidoService.guardar(any(Pedido.class))).thenReturn(pedido);

        mockMvc.perform(post("/api/pedidos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        verify(pedidoService, times(1)).guardar(any(Pedido.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void actualizarPedido_CuandoExiste_DeberiaActualizarYRetornarPedido() throws Exception {
        Pedido pedidoActualizado = Pedido.builder()
                .id(1L)
                .fecha(LocalDateTime.now())
                .estado("COMPLETADO")
                .cliente(cliente)
                .build();

        when(pedidoService.obtenerPorId(1L)).thenReturn(Optional.of(pedido));
        when(pedidoService.guardar(any(Pedido.class))).thenReturn(pedidoActualizado);

        mockMvc.perform(put("/api/pedidos/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("COMPLETADO"));

        verify(pedidoService, times(1)).obtenerPorId(1L);
        verify(pedidoService, times(1)).guardar(any(Pedido.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void actualizarPedido_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        when(pedidoService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/pedidos/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isNotFound());

        verify(pedidoService, times(1)).obtenerPorId(999L);
        verify(pedidoService, never()).guardar(any(Pedido.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void eliminarPedido_CuandoExiste_DeberiaEliminarYRetornar200() throws Exception {
        when(pedidoService.obtenerPorId(1L)).thenReturn(Optional.of(pedido));
        doNothing().when(pedidoService).eliminar(1L);

        mockMvc.perform(delete("/api/pedidos/1")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(pedidoService, times(1)).obtenerPorId(1L);
        verify(pedidoService, times(1)).eliminar(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void eliminarPedido_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        when(pedidoService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/pedidos/999")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(pedidoService, times(1)).obtenerPorId(999L);
        verify(pedidoService, never()).eliminar(anyLong());
    }
}
