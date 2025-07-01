package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.security.JwtUtil;
import com.letrasypapeles.backend.service.ClienteService;
import com.letrasypapeles.backend.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AuthController authController;

    @Mock
    private ClienteService clienteService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{\"email\":\"testuser@example.com\",\"contra\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegister() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"nombre\":\"Test\",\"apellido\":\"User\",\"email\":\"newuser@example.com\",\"contraseña\":\"password\",\"rol\":\"CLIENTE\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterWithInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"nombre\":\"Test\",\"apellido\":\"User\",\"email\":\"invalid-email\",\"contraseña\":\"password\",\"rol\":\"CLIENTE\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterWithEmptyFields() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"nombre\":\"\",\"apellido\":\"\",\"email\":\"\",\"contraseña\":\"\",\"rol\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterWithDuplicateEmail() throws Exception {
        when(clienteService.registrarClienteConRol(any(Cliente.class), eq("CLIENTE")))
                .thenThrow(new RuntimeException("El correo electrónico ya está registrado."));

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"nombre\":\"Test\",\"apellido\":\"User\",\"email\":\"existing@example.com\",\"contraseña\":\"password\",\"rol\":\"CLIENTE\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{\"email\":\"invalid@example.com\",\"contra\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testRegisterWithDifferentRoles() throws Exception {
        String[] roles = {"EMPLEADO", "GERENTE", "ADMIN"};
        
        for (String rol : roles) {
            Cliente mockCliente = new Cliente();
            mockCliente.setNombre("Test");
            mockCliente.setApellido("User");
            mockCliente.setEmail("user" + rol + "@example.com");
            when(clienteService.registrarClienteConRol(any(Cliente.class), eq(rol)))
                    .thenReturn(mockCliente);

            mockMvc.perform(post("/api/auth/register")
                    .contentType("application/json")
                    .content("{\"nombre\":\"Test\",\"apellido\":\"User\",\"email\":\"user" + rol + "@example.com\",\"contraseña\":\"password\",\"rol\":\"" + rol + "\"}"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    public void testRegisterReturnsNullCliente() throws Exception {
        when(clienteService.registrarClienteConRol(any(Cliente.class), eq("CLIENTE")))
                .thenReturn(null);

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"nombre\":\"Test\",\"apellido\":\"User\",\"email\":\"null@example.com\",\"contraseña\":\"password\",\"rol\":\"CLIENTE\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterSuccessfullyCreatesClienteAndClearsPassword() throws Exception {
        Cliente mockCliente = new Cliente();
        mockCliente.setNombre("Test");
        mockCliente.setApellido("User");
        mockCliente.setEmail("success@example.com");
        mockCliente.setContraseña("hashedPassword"); // Simula contraseña hasheada
        
        when(clienteService.registrarClienteConRol(any(Cliente.class), eq("CLIENTE")))
                .thenReturn(mockCliente);

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"nombre\":\"Test\",\"apellido\":\"User\",\"email\":\"success@example.com\",\"contraseña\":\"password\",\"rol\":\"CLIENTE\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginWithAuthentication() throws Exception {
        // Mock successful authentication
        when(authenticationManager.authenticate(any()))
                .thenReturn(mock(Authentication.class));
        
        // Mock UserDetails
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(usuarioService.loadUserByUsername("test@example.com"))
                .thenReturn(mockUserDetails);
        
        // Mock JWT generation
        when(jwtUtil.generateToken(mockUserDetails))
                .thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{\"email\":\"test@example.com\",\"contra\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    public void testRegisterWithMissingFields() throws Exception {
        // Test con solo algunos campos faltantes
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"nombre\":\"Test\",\"email\":\"test@example.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRegisterWithInvalidRuntimeException() throws Exception {
        when(clienteService.registrarClienteConRol(any(Cliente.class), eq("CLIENTE")))
                .thenThrow(new RuntimeException("Error inesperado del sistema"));

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content("{\"nombre\":\"Test\",\"apellido\":\"User\",\"email\":\"error@example.com\",\"contraseña\":\"password\",\"rol\":\"CLIENTE\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginWithBadCredentialsException() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content("{\"email\":\"bad@example.com\",\"contra\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());
    }

    // Tests for inner classes to improve branch coverage
    @Test
    public void testLoginRequestConstructors() {
        // Test no-args constructor
        AuthController.LoginRequest loginRequest1 = new AuthController.LoginRequest();
        assertNotNull(loginRequest1);
        
        // Test all-args constructor
        AuthController.LoginRequest loginRequest2 = new AuthController.LoginRequest("test@email.com", "password");
        assertEquals("test@email.com", loginRequest2.getEmail());
        assertEquals("password", loginRequest2.getContra());
        
        // Test setters
        loginRequest1.setEmail("another@email.com");
        loginRequest1.setContra("newpass");
        assertEquals("another@email.com", loginRequest1.getEmail());
        assertEquals("newpass", loginRequest1.getContra());
        
        // Test equals - same object
        assertEquals(loginRequest2, loginRequest2);
        
        // Test equals - equivalent objects
        AuthController.LoginRequest loginRequest3 = new AuthController.LoginRequest("test@email.com", "password");
        assertEquals(loginRequest2, loginRequest3);
        assertEquals(loginRequest2.hashCode(), loginRequest3.hashCode());
        
        // Test equals - different objects
        AuthController.LoginRequest loginRequest4 = new AuthController.LoginRequest("different@email.com", "password");
        assertNotEquals(loginRequest2, loginRequest4);
        
        AuthController.LoginRequest loginRequest5 = new AuthController.LoginRequest("test@email.com", "different");
        assertNotEquals(loginRequest2, loginRequest5);
        
        // Test equals - null
        assertNotEquals(loginRequest2, null);
        
        // Test equals - different class
        assertNotEquals(loginRequest2, "string");
        
        // Test canEqual
        assertTrue(loginRequest2.canEqual(loginRequest3));
        assertFalse(loginRequest2.canEqual("string"));
        
        // Test with null fields
        AuthController.LoginRequest loginNull1 = new AuthController.LoginRequest();
        AuthController.LoginRequest loginNull2 = new AuthController.LoginRequest();
        assertEquals(loginNull1, loginNull2);
        assertEquals(loginNull1.hashCode(), loginNull2.hashCode());
        
        loginNull1.setEmail("test@email.com");
        assertNotEquals(loginNull1, loginNull2);
        
        loginNull2.setEmail("test@email.com");
        assertEquals(loginNull1, loginNull2);
        
        // Test toString
        String toString = loginRequest2.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("email"));
    }

    @Test
    public void testLoginResponseConstructors() {
        // Test no-args constructor
        AuthController.LoginResponse response1 = new AuthController.LoginResponse();
        assertNotNull(response1);
        
        // Test all-args constructor
        AuthController.LoginResponse response2 = new AuthController.LoginResponse("token123");
        assertEquals("token123", response2.getToken());
        
        // Test setter
        response1.setToken("newtoken");
        assertEquals("newtoken", response1.getToken());
        
        // Test equals - same object
        assertEquals(response2, response2);
        
        // Test equals - equivalent objects
        AuthController.LoginResponse response3 = new AuthController.LoginResponse("token123");
        assertEquals(response2, response3);
        assertEquals(response2.hashCode(), response3.hashCode());
        
        // Test equals - different objects
        AuthController.LoginResponse response4 = new AuthController.LoginResponse("differenttoken");
        assertNotEquals(response2, response4);
        
        // Test equals - null
        assertNotEquals(response2, null);
        
        // Test equals - different class
        assertNotEquals(response2, "string");
        
        // Test canEqual
        assertTrue(response2.canEqual(response3));
        assertFalse(response2.canEqual("string"));
        
        // Test with null token
        AuthController.LoginResponse responseNull1 = new AuthController.LoginResponse();
        AuthController.LoginResponse responseNull2 = new AuthController.LoginResponse();
        assertEquals(responseNull1, responseNull2);
        assertEquals(responseNull1.hashCode(), responseNull2.hashCode());
        
        responseNull1.setToken("test");
        assertNotEquals(responseNull1, responseNull2);
        
        responseNull2.setToken("test");
        assertEquals(responseNull1, responseNull2);
        
        // Test toString
        String toString = response2.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("token"));
    }

    @Test
    public void testRegisterRequestConstructors() {
        // Test no-args constructor
        AuthController.RegisterRequest request1 = new AuthController.RegisterRequest();
        assertNotNull(request1);
        
        // Test all-args constructor
        AuthController.RegisterRequest request2 = new AuthController.RegisterRequest(
            "Juan", "Pérez", "juan@email.com", "password123", "CLIENTE"
        );
        assertEquals("Juan", request2.getNombre());
        assertEquals("Pérez", request2.getApellido());
        assertEquals("juan@email.com", request2.getEmail());
        assertEquals("password123", request2.getContraseña());
        assertEquals("CLIENTE", request2.getRol());
        
        // Test setters
        request1.setNombre("Ana");
        request1.setApellido("García");
        request1.setEmail("ana@email.com");
        request1.setContraseña("pass456");
        request1.setRol("EMPLEADO");
        
        assertEquals("Ana", request1.getNombre());
        assertEquals("García", request1.getApellido());
        assertEquals("ana@email.com", request1.getEmail());
        assertEquals("pass456", request1.getContraseña());
        assertEquals("EMPLEADO", request1.getRol());
        
        // Test equals and hashCode - same objects
        assertEquals(request2, request2);
        assertEquals(request2.hashCode(), request2.hashCode());
        
        // Test equals - equivalent objects
        AuthController.RegisterRequest request3 = new AuthController.RegisterRequest(
            "Juan", "Pérez", "juan@email.com", "password123", "CLIENTE"
        );
        assertEquals(request2, request3);
        assertEquals(request2.hashCode(), request3.hashCode());
        
        // Test equals - null
        assertNotEquals(request2, null);
        
        // Test equals - different class
        assertNotEquals(request2, "string");
        
        // Test canEqual
        assertTrue(request2.canEqual(request3));
        assertFalse(request2.canEqual("string"));
        
        // Test equals with different fields
        AuthController.RegisterRequest request4 = new AuthController.RegisterRequest(
            "Pedro", "Pérez", "juan@email.com", "password123", "CLIENTE"
        );
        assertNotEquals(request2, request4);
        
        AuthController.RegisterRequest request5 = new AuthController.RegisterRequest(
            "Juan", "García", "juan@email.com", "password123", "CLIENTE"
        );
        assertNotEquals(request2, request5);
        
        AuthController.RegisterRequest request6 = new AuthController.RegisterRequest(
            "Juan", "Pérez", "pedro@email.com", "password123", "CLIENTE"
        );
        assertNotEquals(request2, request6);
        
        AuthController.RegisterRequest request7 = new AuthController.RegisterRequest(
            "Juan", "Pérez", "juan@email.com", "different", "CLIENTE"
        );
        assertNotEquals(request2, request7);
        
        AuthController.RegisterRequest request8 = new AuthController.RegisterRequest(
            "Juan", "Pérez", "juan@email.com", "password123", "ADMIN"
        );
        assertNotEquals(request2, request8);
        
        // Test with null fields
        AuthController.RegisterRequest requestNull1 = new AuthController.RegisterRequest();
        AuthController.RegisterRequest requestNull2 = new AuthController.RegisterRequest();
        assertEquals(requestNull1, requestNull2);
        
        requestNull1.setNombre("Test");
        assertNotEquals(requestNull1, requestNull2);
        
        requestNull2.setNombre("Test");
        assertEquals(requestNull1, requestNull2);
        
        // Test toString
        String toString = request2.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("nombre"));
    }
}
