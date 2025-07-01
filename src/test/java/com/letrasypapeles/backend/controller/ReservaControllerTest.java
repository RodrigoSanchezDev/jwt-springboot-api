package com.letrasypapeles.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Producto;

import java.math.BigDecimal;
import com.letrasypapeles.backend.service.ReservaService;
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
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reserva reserva;
    private Cliente cliente;
    private Producto producto;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan Pérez")
                .email("juan@email.com")
                .build();

        producto = Producto.builder()
                .id(1L)
                .nombre("Cuaderno")
                .precio(BigDecimal.valueOf(15.5))
                .stock(100)
                .build();

        reserva = Reserva.builder()
                .id(1L)
                .fechaReserva(LocalDateTime.now())
                .estado("ACTIVA")
                .cantidad(2)
                .cliente(cliente)
                .producto(producto)
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    void obtenerTodas_DeberiaRetornarListaDeReservas() throws Exception {
        List<Reserva> reservas = Arrays.asList(reserva);
        when(reservaService.obtenerTodas()).thenReturn(reservas);

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].estado").value("ACTIVA"))
                .andExpect(jsonPath("$[0].cantidad").value(2));

        verify(reservaService, times(1)).obtenerTodas();
    }

    @Test
    @WithMockUser(roles = "USER")
    void obtenerPorId_CuandoExiste_DeberiaRetornarReserva() throws Exception {
        when(reservaService.obtenerPorId(1L)).thenReturn(Optional.of(reserva));

        mockMvc.perform(get("/api/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                .andExpect(jsonPath("$.cantidad").value(2));

        verify(reservaService, times(1)).obtenerPorId(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void obtenerPorId_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        when(reservaService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservas/999"))
                .andExpect(status().isNotFound());

        verify(reservaService, times(1)).obtenerPorId(999L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void obtenerPorClienteId_DeberiaRetornarReservasDelCliente() throws Exception {
        List<Reserva> reservas = Arrays.asList(reserva);
        when(reservaService.obtenerPorClienteId(1L)).thenReturn(reservas);

        mockMvc.perform(get("/api/reservas/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].cliente.id").value(1));

        verify(reservaService, times(1)).obtenerPorClienteId(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void crearReserva_DeberiaCrearYRetornarReserva() throws Exception {
        Reserva nuevaReserva = Reserva.builder()
                .fechaReserva(LocalDateTime.now())
                .estado("ACTIVA")
                .cantidad(3)
                .cliente(cliente)
                .producto(producto)
                .build();

        Reserva reservaGuardada = Reserva.builder()
                .id(2L)
                .fechaReserva(nuevaReserva.getFechaReserva())
                .estado("ACTIVA")
                .cantidad(3)
                .cliente(cliente)
                .producto(producto)
                .build();

        when(reservaService.guardar(any(Reserva.class))).thenReturn(reservaGuardada);

        mockMvc.perform(post("/api/reservas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaReserva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                .andExpect(jsonPath("$.cantidad").value(3));

        verify(reservaService, times(1)).guardar(any(Reserva.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void actualizarReserva_CuandoExiste_DeberiaActualizarYRetornar() throws Exception {
        Reserva reservaActualizada = Reserva.builder()
                .id(1L)
                .fechaReserva(LocalDateTime.now())
                .estado("COMPLETADA")
                .cantidad(5)
                .cliente(cliente)
                .producto(producto)
                .build();

        when(reservaService.obtenerPorId(1L)).thenReturn(Optional.of(reserva));
        when(reservaService.guardar(any(Reserva.class))).thenReturn(reservaActualizada);

        mockMvc.perform(put("/api/reservas/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("COMPLETADA"))
                .andExpect(jsonPath("$.cantidad").value(5));

        verify(reservaService, times(1)).obtenerPorId(1L);
        verify(reservaService, times(1)).guardar(any(Reserva.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void actualizarReserva_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        Reserva reservaActualizada = Reserva.builder()
                .fechaReserva(LocalDateTime.now())
                .estado("COMPLETADA")
                .cantidad(5)
                .cliente(cliente)
                .producto(producto)
                .build();

        when(reservaService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/reservas/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaActualizada)))
                .andExpect(status().isNotFound());

        verify(reservaService, times(1)).obtenerPorId(999L);
        verify(reservaService, never()).guardar(any(Reserva.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void eliminarReserva_CuandoExiste_DeberiaEliminarYRetornar200() throws Exception {
        when(reservaService.obtenerPorId(1L)).thenReturn(Optional.of(reserva));

        mockMvc.perform(delete("/api/reservas/1")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(reservaService, times(1)).obtenerPorId(1L);
        verify(reservaService, times(1)).eliminar(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void eliminarReserva_CuandoNoExiste_DeberiaRetornar404() throws Exception {
        when(reservaService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/reservas/999")
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(reservaService, times(1)).obtenerPorId(999L);
        verify(reservaService, never()).eliminar(anyLong());
    }
}
