package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

class ClienteTest {

    private Cliente cliente;
    private Role role1;
    private Role role2;

    @BeforeEach
    void setUp() {
        role1 = Role.builder()
                .nombre("CLIENTE")
                .build();

        role2 = Role.builder()
                .nombre("ADMIN")
                .build();

        cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@example.com")
                .contraseña("password123")
                .puntosFidelidad(100)
                .roles(new HashSet<>())
                .build();
    }

    @Test
    void testClienteBuilder() {
        assertNotNull(cliente);
        assertEquals(1L, cliente.getId());
        assertEquals("Juan", cliente.getNombre());
        assertEquals("Pérez", cliente.getApellido());
        assertEquals("juan@example.com", cliente.getEmail());
        assertEquals("password123", cliente.getContraseña());
        assertEquals(100, cliente.getPuntosFidelidad());
        assertNotNull(cliente.getRoles());
        assertTrue(cliente.getRoles().isEmpty());
    }

    @Test
    void testAgregarRol() {
        cliente.agregarRol(role1);

        assertEquals(1, cliente.getRoles().size());
        assertTrue(cliente.getRoles().contains(role1));
    }

    @Test
    void testAgregarMultiplesRoles() {
        cliente.agregarRol(role1);
        cliente.agregarRol(role2);

        assertEquals(2, cliente.getRoles().size());
        assertTrue(cliente.getRoles().contains(role1));
        assertTrue(cliente.getRoles().contains(role2));
    }

    @Test
    void testEliminarRol() {
        cliente.agregarRol(role1);
        cliente.agregarRol(role2);

        cliente.eliminarRol(role1);

        assertEquals(1, cliente.getRoles().size());
        assertFalse(cliente.getRoles().contains(role1));
        assertTrue(cliente.getRoles().contains(role2));
    }

    @Test
    void testEliminarRolInexistente() {
        cliente.agregarRol(role1);

        cliente.eliminarRol(role2); // role2 no está agregado

        assertEquals(1, cliente.getRoles().size());
        assertTrue(cliente.getRoles().contains(role1));
    }

    @Test
    void testObtenerRoles() {
        cliente.agregarRol(role1);
        cliente.agregarRol(role2);

        Set<Role> roles = cliente.obtenerRoles();

        assertEquals(2, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
    }

    @Test
    void testObtenerRolesSinRoles() {
        Set<Role> roles = cliente.obtenerRoles();

        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }

    @Test
    void testConstructorVacio() {
        Cliente clienteVacio = new Cliente();

        assertNotNull(clienteVacio);
        assertNull(clienteVacio.getId());
        assertNull(clienteVacio.getNombre());
        assertNull(clienteVacio.getEmail());
    }

    @Test
    void testClienteConRolesInicial() {
        Set<Role> rolesIniciales = new HashSet<>();
        rolesIniciales.add(role1);

        Cliente clienteConRoles = Cliente.builder()
                .id(2L)
                .nombre("María")
                .email("maria@example.com")
                .roles(rolesIniciales)
                .build();

        assertEquals(1, clienteConRoles.getRoles().size());
        assertTrue(clienteConRoles.getRoles().contains(role1));
    }

    @Test
    public void testAsignacionDePuntosDeFidelidad() {
        Cliente cliente = new Cliente();
        cliente.setPuntosFidelidad(100);
        assertEquals(100, cliente.getPuntosFidelidad());
    }

    @Test
    public void testEquals() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().nombre("CLIENTE").build());

        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan@example.com")
                .contraseña("password123")
                .puntosFidelidad(100)
                .roles(roles)
                .build();

        Cliente cliente2 = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan@example.com")
                .contraseña("password123")
                .puntosFidelidad(100)
                .roles(roles)
                .build();

        Cliente cliente3 = Cliente.builder()
                .id(2L)
                .nombre("Maria")
                .apellido("Lopez")
                .email("maria@example.com")
                .contraseña("password456")
                .puntosFidelidad(200)
                .roles(new HashSet<>())
                .build();

        // Test equals - same objects
        assertEquals(cliente1, cliente1);

        // Test equals - equivalent objects
        assertEquals(cliente1, cliente2);

        // Test equals - different objects
        assertNotEquals(cliente1, cliente3);

        // Test equals - null
        assertNotEquals(cliente1, null);

        // Test equals - different class
        assertNotEquals(cliente1, "string");

        // Test canEqual method
        assertTrue(cliente1.canEqual(cliente2));
        assertFalse(cliente1.canEqual("string"));
    }

    @Test
    public void testEqualsWithDifferentFields() {
        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .build();

        Cliente cliente2 = Cliente.builder()
                .id(1L)
                .nombre("Pedro")
                .build();

        // Should be different because nombres are different
        assertNotEquals(cliente1, cliente2);

        // Test with different apellidos
        cliente1.setApellido("Perez");
        cliente2.setNombre("Juan");
        cliente2.setApellido("Lopez");
        assertNotEquals(cliente1, cliente2);

        // Test with different emails
        cliente1.setEmail("juan@example.com");
        cliente2.setApellido("Perez");
        cliente2.setEmail("pedro@example.com");
        assertNotEquals(cliente1, cliente2);

        // Test with different contraseñas
        cliente1.setContraseña("password123");
        cliente2.setEmail("juan@example.com");
        cliente2.setContraseña("password456");
        assertNotEquals(cliente1, cliente2);

        // Test with different puntos fidelidad
        cliente1.setPuntosFidelidad(100);
        cliente2.setContraseña("password123");
        cliente2.setPuntosFidelidad(200);
        assertNotEquals(cliente1, cliente2);
    }

    @Test
    public void testEqualsWithNullFields() {
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);

        Cliente cliente2 = new Cliente();
        cliente2.setId(1L);

        // Test equals with null fields
        assertEquals(cliente1, cliente2);

        // Test with one null, one non-null nombre
        cliente1.setNombre("Juan");
        assertNotEquals(cliente1, cliente2);

        // Test with both having same nombre
        cliente2.setNombre("Juan");
        assertEquals(cliente1, cliente2);

        // Test with different nombres
        cliente2.setNombre("Pedro");
        assertNotEquals(cliente1, cliente2);
    }

    @Test
    public void testHashCodeWithDifferentFields() {
        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan@example.com")
                .contraseña("password123")
                .puntosFidelidad(100)
                .build();

        Cliente cliente2 = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Perez")
                .email("juan@example.com")
                .contraseña("password123")
                .puntosFidelidad(100)
                .build();

        // Test hashCode consistency
        assertEquals(cliente1.hashCode(), cliente2.hashCode());

        // Test with different values
        cliente2.setNombre("Pedro");
        assertNotEquals(cliente1.hashCode(), cliente2.hashCode());
    }

    @Test
    public void testHashCodeWithNullFields() {
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();

        // Test hashCode with null fields
        assertEquals(cliente1.hashCode(), cliente2.hashCode());

        // Test with one field set
        cliente1.setNombre("Juan");
        assertNotEquals(cliente1.hashCode(), cliente2.hashCode());

        // Test with same field set
        cliente2.setNombre("Juan");
        assertEquals(cliente1.hashCode(), cliente2.hashCode());
    }

    @Test
    public void testEqualsWithDifferentRoles() {
        Set<Role> roles1 = new HashSet<>();
        roles1.add(Role.builder().nombre("CLIENTE").build());

        Set<Role> roles2 = new HashSet<>();
        roles2.add(Role.builder().nombre("ADMIN").build());

        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .roles(roles1)
                .build();

        Cliente cliente2 = Cliente.builder()
                .id(1L)
                .roles(roles2)
                .build();

        // Should be different because roles are different
        assertNotEquals(cliente1, cliente2);
    }
    
    @Test
    void testClienteBuilderToString() {
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        
        Cliente.ClienteBuilder builder = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan@example.com")
                .contraseña("password123")
                .puntosFidelidad(100)
                .roles(roles);
        
        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("ClienteBuilder"));
    }

    @Test
    void testEqualsAndHashCodeEdgeCases() {
        // Test equals with same ID but all other fields null
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        Cliente cliente2 = new Cliente();
        cliente2.setId(1L);
        
        assertEquals(cliente1, cliente2);
        assertEquals(cliente1.hashCode(), cliente2.hashCode());
        
        // Test with different IDs
        cliente2.setId(2L);
        assertNotEquals(cliente1, cliente2);
        
        // Test canEqual with different objects
        assertFalse(cliente1.canEqual(new Object()));
        assertTrue(cliente1.canEqual(cliente2));
        
        // Test all null vs some null cases
        Cliente clienteNulo = new Cliente();
        Cliente clienteConNombre = new Cliente();
        clienteConNombre.setNombre("Juan");
        
        assertNotEquals(clienteNulo, clienteConNombre);
        assertNotEquals(clienteConNombre, clienteNulo);
        
        // Test diferentes combinaciones de campos nulos vs no nulos
        Cliente c1 = new Cliente();
        c1.setApellido("Perez");
        Cliente c2 = new Cliente();
        assertNotEquals(c1, c2);
        
        c1 = new Cliente();
        c1.setEmail("test@email.com");
        c2 = new Cliente();
        assertNotEquals(c1, c2);
        
        c1 = new Cliente();
        c1.setContraseña("password");
        c2 = new Cliente();
        assertNotEquals(c1, c2);
        
        c1 = new Cliente();
        c1.setPuntosFidelidad(100);
        c2 = new Cliente();
        c2.setPuntosFidelidad(200);
        assertNotEquals(c1, c2);
    }
    
    @Test
    void testEqualsAndHashCodeWithRolesNull() {
        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .roles(null)
                .build();
                
        Cliente cliente2 = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .roles(null)
                .build();
                
        assertEquals(cliente1, cliente2);
        assertEquals(cliente1.hashCode(), cliente2.hashCode());
        
        // One null, one empty set
        Set<Role> rolesVacios = new HashSet<>();
        cliente2.setRoles(rolesVacios);
        assertNotEquals(cliente1, cliente2);
        
        // One null, one with roles
        Set<Role> rolesConDatos = new HashSet<>();
        rolesConDatos.add(role1);
        cliente2.setRoles(rolesConDatos);
        assertNotEquals(cliente1, cliente2);
    }
}
