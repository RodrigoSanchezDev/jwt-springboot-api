package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.ClienteRepository;
import com.letrasypapeles.backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarCliente() {
        Cliente cliente = Cliente.builder()
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .contraseña("password")
                .build();

        // Mock del rol CLIENTE
        Role rolCliente = Role.builder()
                .nombre("CLIENTE")
                .build();

        // Cliente con contraseña encriptada para el retorno del save
        Cliente clienteGuardado = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .contraseña("hashedPassword")
                .puntosFidelidad(0)
                .build();

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(cliente.getContraseña())).thenReturn("hashedPassword");
        when(roleRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(rolCliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        Cliente result = clienteService.registrarCliente(cliente);

        assertNotNull(result);
        assertEquals("hashedPassword", result.getContraseña());
        assertEquals(0, result.getPuntosFidelidad());
    }

    @Test
    void testObtenerPorEmail() {
        Cliente cliente = Cliente.builder()
                .email("juan.perez@example.com")
                .build();

        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));

        Optional<Cliente> result = clienteService.obtenerPorEmail("juan.perez@example.com");

        assertTrue(result.isPresent());
        assertEquals("juan.perez@example.com", result.get().getEmail());
    }

    @Test
    void testRegistrarClienteEmailDuplicado() {
        Cliente cliente = Cliente.builder()
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .contraseña("password")
                .build();

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.registrarCliente(cliente);
        });

        assertEquals("El correo electrónico ya está registrado.", exception.getMessage());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testRegistrarClienteRoleNoExiste() {
        Cliente cliente = Cliente.builder()
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .contraseña("password")
                .build();

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(cliente.getContraseña())).thenReturn("hashedPassword");
        when(roleRepository.findByNombre("CLIENTE")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.registrarCliente(cliente);
        });

        assertEquals("El rol 'CLIENTE' no existe en la BD.", exception.getMessage());
    }

    @Test
    void testRegistrarClienteConRol() {
        Cliente cliente = Cliente.builder()
                .nombre("Ana")
                .apellido("García")
                .email("ana.garcia@example.com")
                .contraseña("password123")
                .build();

        Role rolAdmin = Role.builder()
                .nombre("ADMIN")
                .build();

        Cliente clienteGuardado = Cliente.builder()
                .id(2L)
                .nombre("Ana")
                .apellido("García")
                .email("ana.garcia@example.com")
                .contraseña("hashedPassword123")
                .puntosFidelidad(0)
                .build();

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(cliente.getContraseña())).thenReturn("hashedPassword123");
        when(roleRepository.findByNombre("ADMIN")).thenReturn(Optional.of(rolAdmin));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(clienteGuardado));

        Cliente result = clienteService.registrarClienteConRol(cliente, "admin");

        assertNotNull(result);
        assertEquals("hashedPassword123", result.getContraseña());
        assertEquals(0, result.getPuntosFidelidad());
        verify(roleRepository).findByNombre("ADMIN");
    }

    @Test
    void testRegistrarClienteConRolEmailDuplicado() {
        Cliente cliente = Cliente.builder()
                .email("duplicado@example.com")
                .contraseña("password")
                .build();

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.registrarClienteConRol(cliente, "EMPLEADO");
        });

        assertEquals("El correo electrónico ya está registrado.", exception.getMessage());
    }

    @Test
    void testRegistrarClienteConRolPasswordNull() {
        Cliente cliente = Cliente.builder()
                .email("test@example.com")
                .contraseña(null)
                .build();

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.registrarClienteConRol(cliente, "CLIENTE");
        });

        assertEquals("La contraseña no puede ser nula o vacía.", exception.getMessage());
    }

    @Test
    void testRegistrarClienteConRolPasswordBlank() {
        Cliente cliente = Cliente.builder()
                .email("test@example.com")
                .contraseña("   ")
                .build();

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.registrarClienteConRol(cliente, "CLIENTE");
        });

        assertEquals("La contraseña no puede ser nula o vacía.", exception.getMessage());
    }

    @Test
    void testRegistrarClienteConRolNullDefaultsToCliente() {
        Cliente cliente = Cliente.builder()
                .nombre("Default")
                .apellido("User")
                .email("default@example.com")
                .contraseña("password")
                .build();

        Role rolCliente = Role.builder()
                .nombre("CLIENTE")
                .build();

        Cliente clienteGuardado = Cliente.builder()
                .id(3L)
                .nombre("Default")
                .apellido("User")
                .email("default@example.com")
                .contraseña("hashedPassword")
                .puntosFidelidad(0)
                .build();

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(cliente.getContraseña())).thenReturn("hashedPassword");
        when(roleRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(rolCliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(clienteGuardado));

        Cliente result = clienteService.registrarClienteConRol(cliente, null);

        assertNotNull(result);
        verify(roleRepository).findByNombre("CLIENTE");
    }

    @Test
    void testRegistrarClienteConRolBlankDefaultsToCliente() {
        Cliente cliente = Cliente.builder()
                .email("blank@example.com")
                .contraseña("password")
                .build();

        Role rolCliente = Role.builder()
                .nombre("CLIENTE")
                .build();

        Cliente clienteGuardado = Cliente.builder()
                .id(4L)
                .email("blank@example.com")
                .contraseña("hashedPassword")
                .puntosFidelidad(0)
                .build();

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(cliente.getContraseña())).thenReturn("hashedPassword");
        when(roleRepository.findByNombre("CLIENTE")).thenReturn(Optional.of(rolCliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(clienteGuardado));

        Cliente result = clienteService.registrarClienteConRol(cliente, "   ");

        assertNotNull(result);
        verify(roleRepository).findByNombre("CLIENTE");
    }

    @Test
    void testRegistrarClienteConRolTrimsAndUppercases() {
        Cliente cliente = Cliente.builder()
                .email("uppercase@example.com")
                .contraseña("password")
                .build();

        Role rolGerente = Role.builder()
                .nombre("GERENTE")
                .build();

        Cliente clienteGuardado = Cliente.builder()
                .id(5L)
                .email("uppercase@example.com")
                .contraseña("hashedPassword")
                .puntosFidelidad(0)
                .build();

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(cliente.getContraseña())).thenReturn("hashedPassword");
        when(roleRepository.findByNombre("GERENTE")).thenReturn(Optional.of(rolGerente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(clienteGuardado));

        Cliente result = clienteService.registrarClienteConRol(cliente, "  gerente  ");

        assertNotNull(result);
        verify(roleRepository).findByNombre("GERENTE");
    }

    @Test
    void testRegistrarClienteConRolRoleNoExiste() {
        Cliente cliente = Cliente.builder()
                .email("noexiste@example.com")
                .contraseña("password")
                .build();

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(cliente.getContraseña())).thenReturn("hashedPassword");
        when(roleRepository.findByNombre("INEXISTENTE")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.registrarClienteConRol(cliente, "INEXISTENTE");
        });

        assertEquals("El rol 'INEXISTENTE' no existe en la BD.", exception.getMessage());
    }

    @Test
    void testObtenerTodos() {
        when(clienteRepository.findAll()).thenReturn(java.util.List.of());

        java.util.List<Cliente> result = clienteService.obtenerTodos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clienteRepository).findAll();
    }

    @Test
    void testObtenerPorId() {
        Cliente cliente = Cliente.builder()
                .id(1L)
                .email("test@example.com")
                .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> result = clienteService.obtenerPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(clienteRepository).findById(1L);
    }

    @Test
    void testActualizarCliente() {
        Cliente cliente = Cliente.builder()
                .id(1L)
                .email("updated@example.com")
                .build();

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.actualizarCliente(cliente);

        assertNotNull(result);
        assertEquals("updated@example.com", result.getEmail());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testEliminar() {
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.eliminar(1L);

        verify(clienteRepository).deleteById(1L);
    }
}
