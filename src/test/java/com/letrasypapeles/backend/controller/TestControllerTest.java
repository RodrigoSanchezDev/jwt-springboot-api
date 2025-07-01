package com.letrasypapeles.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "CLIENTE")
    void testSoloCliente() throws Exception {
        mockMvc.perform(get("/api/test/cliente"))
                .andExpect(status().isOk())
                .andExpect(content().string("¡Hola CLIENTE!"));
    }

    @Test
    @WithMockUser(authorities = "EMPLEADO")
    void testSoloEmpleado() throws Exception {
        mockMvc.perform(get("/api/test/empleado"))
                .andExpect(status().isOk())
                .andExpect(content().string("¡Hola EMPLEADO!"));
    }

    @Test
    @WithMockUser(authorities = "GERENTE")
    void testSoloGerente() throws Exception {
        mockMvc.perform(get("/api/test/gerente"))
                .andExpect(status().isOk())
                .andExpect(content().string("¡Hola GERENTE!"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testAdmin() throws Exception {
        mockMvc.perform(get("/api/test/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hola ADMIN, este endpoint es sólo para ti."));
    }
}
