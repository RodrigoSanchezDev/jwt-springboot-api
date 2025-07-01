package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Test
    @WithMockUser
    void testObtenerTodos() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .build());
        clientes.add(Cliente.builder()
                .id(2L)
                .nombre("Ana")
                .apellido("García")
                .email("ana.garcia@example.com")
                .build());

        Mockito.when(clienteService.obtenerTodos()).thenReturn(clientes);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[1].nombre").value("Ana"));
    }

    @Test
    @WithMockUser
    void testObtenerTodosListaVacia() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        Mockito.when(clienteService.obtenerTodos()).thenReturn(clientes);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser // Basic authentication for endpoints that require .authenticated()
    void testObtenerPorId() throws Exception {
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .build();

        Mockito.when(clienteService.obtenerPorId(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"));
    }

    @Test
    @WithMockUser
    void testObtenerPorIdNoEncontrado() throws Exception {
        Mockito.when(clienteService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser // Basic authentication for endpoints that require .authenticated()
    void testRegistrarCliente() throws Exception {
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .build();

        Mockito.when(clienteService.registrarCliente(Mockito.any(Cliente.class))).thenReturn(cliente);

        String jsonContent = "{\"nombre\":\"Juan\",\"apellido\":\"Perez\",\"email\":\"juan.perez@example.com\"}";

        mockMvc.perform(post("/api/clientes/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"));
    }

    @Test
    @WithMockUser
    void testActualizarCliente() throws Exception {
        Cliente clienteExistente = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .build();

        Cliente clienteActualizado = Cliente.builder()
                .id(1L)
                .nombre("Juan Carlos")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .build();

        Mockito.when(clienteService.obtenerPorId(1L)).thenReturn(Optional.of(clienteExistente));
        Mockito.when(clienteService.actualizarCliente(Mockito.any(Cliente.class))).thenReturn(clienteActualizado);

        String jsonContent = "{\"nombre\":\"Juan Carlos\",\"apellido\":\"Perez\",\"email\":\"juan.perez@example.com\"}";

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Carlos"));
    }

    @Test
    @WithMockUser
    void testActualizarClienteNoEncontrado() throws Exception {
        Mockito.when(clienteService.obtenerPorId(999L)).thenReturn(Optional.empty());

        String jsonContent = "{\"nombre\":\"Juan Carlos\",\"apellido\":\"Perez\",\"email\":\"juan.perez@example.com\"}";

        mockMvc.perform(put("/api/clientes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testEliminarCliente() throws Exception {
        Cliente clienteExistente = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .build();

        Mockito.when(clienteService.obtenerPorId(1L)).thenReturn(Optional.of(clienteExistente));
        Mockito.doNothing().when(clienteService).eliminar(1L);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testEliminarClienteNoEncontrado() throws Exception {
        Mockito.when(clienteService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/clientes/999"))
                .andExpect(status().isNotFound());
    }
}