package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .nombre("CLIENTE")
                .build();
    }

    @Test
    void testRoleBuilder() {
        assertNotNull(role);
        assertEquals("CLIENTE", role.getNombre());
    }

    @Test
    void testConstructorVacio() {
        Role roleVacio = new Role();
        
        assertNotNull(roleVacio);
        assertNull(roleVacio.getNombre());
    }

    @Test
    void testSettersAndGetters() {
        Role r = new Role();
        
        r.setNombre("ADMIN");
        
        assertEquals("ADMIN", r.getNombre());
    }

    @Test
    void testRolesValidos() {
        // Probar diferentes tipos de roles válidos
        String[] rolesValidos = {"CLIENTE", "EMPLEADO", "GERENTE", "ADMIN"};
        
        for (String nombreRole : rolesValidos) {
            Role roleTest = Role.builder()
                    .nombre(nombreRole)
                    .build();
            
            assertEquals(nombreRole, roleTest.getNombre());
        }
    }

    @Test
    void testValidarNombreNoVacio() {
        // Validar que el nombre no esté vacío
        assertNotNull(role.getNombre());
        assertFalse(role.getNombre().isEmpty());
    }

    @Test
    void testValidarNombreEnMayusculas() {
        // Validar que el nombre esté en mayúsculas
        assertEquals(role.getNombre().toUpperCase(), role.getNombre());
    }

    @Test
    void testToString() {
        String toString = role.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("CLIENTE"));
    }

    @Test
    void testEqualsAndHashCode() {
        Role role1 = Role.builder()
                .nombre("CLIENTE")
                .build();
                
        Role role2 = Role.builder()
                .nombre("CLIENTE")
                .build();
                
        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    void testEqualsConMismoObjeto() {
        assertEquals(role, role);
    }

    @Test
    void testEqualsConNull() {
        assertNotEquals(role, null);
    }

    @Test
    void testEqualsConClaseDiferente() {
        assertNotEquals(role, "Una string");
        assertNotEquals(role, 123);
    }

    @Test
    void testEqualsConNombreDiferente() {
        Role otroRole = Role.builder()
                .nombre("ADMIN")
                .build();
        
        assertNotEquals(role, otroRole);
        assertNotEquals(role.hashCode(), otroRole.hashCode());
    }

    @Test
    void testEqualsConNombreNull() {
        Role role1 = new Role();
        Role role2 = new Role();
        
        assertEquals(role1, role2);
        
        Role role3 = Role.builder()
                .nombre("TEST")
                .build();
        
        assertNotEquals(role1, role3);
    }

    @Test
    void testCanEqual() {
        assertTrue(role.canEqual(new Role()));
        assertFalse(role.canEqual("Una string"));
        assertFalse(role.canEqual(null));
    }

    @Test
    void testHashCodeConsistencia() {
        int hashCode1 = role.hashCode();
        int hashCode2 = role.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeConNombreNull() {
        Role roleConNull = new Role();
        int hashCode = roleConNull.hashCode();
        // Solo verificamos que no lance excepción
        assertNotNull(hashCode);
    }

    @Test
    void testBuilderMetodos() {
        Role.RoleBuilder builder = Role.builder();
        assertNotNull(builder);
        
        // Test de todos los métodos del builder
        Role roleCompleto = builder
                .nombre("GERENTE")
                .build();
        
        assertEquals("GERENTE", roleCompleto.getNombre());
    }

    @Test
    void testBuilderToString() {
        Role.RoleBuilder builder = Role.builder()
                .nombre("TEST_ROLE");
        
        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("RoleBuilder"));
    }

    @Test
    void testEsRoleCliente() {
        // Método de lógica para verificar si es rol cliente
        boolean esCliente = "CLIENTE".equals(role.getNombre());
        assertTrue(esCliente);
    }

    @Test
    void testEsRoleAdministrativo() {
        // Método de lógica para verificar si es rol administrativo
        Role roleGerente = Role.builder()
                .nombre("GERENTE")
                .build();
                
        boolean esAdministrativo = "GERENTE".equals(roleGerente.getNombre()) || 
                                  "ADMIN".equals(roleGerente.getNombre());
        assertTrue(esAdministrativo);
    }
    
    @Test
    void testRoleBuilderWithClientes() {
        Cliente cliente1 = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .email("juan@test.com")
                .build();
        
        Cliente cliente2 = Cliente.builder()
                .id(2L)
                .nombre("María")
                .email("maria@test.com")
                .build();
        
        java.util.Set<Cliente> clientes = new java.util.HashSet<>();
        clientes.add(cliente1);
        clientes.add(cliente2);
        
        Role.RoleBuilder builder = Role.builder()
                .nombre("CLIENTE")
                .clientes(clientes);
        
        Role roleConClientes = builder.build();
        
        assertNotNull(roleConClientes);
        assertEquals("CLIENTE", roleConClientes.getNombre());
        assertNotNull(roleConClientes.getClientes());
        assertEquals(2, roleConClientes.getClientes().size());
        assertTrue(roleConClientes.getClientes().contains(cliente1));
        assertTrue(roleConClientes.getClientes().contains(cliente2));
    }

    @Test
    void testEqualsAndHashCodeExhaustive() {
        // Casos exhaustivos para equals y hashCode en Role
        
        // Caso 1: Objetos iguales
        Role role1 = Role.builder().nombre("CLIENTE").build();
        Role role2 = Role.builder().nombre("CLIENTE").build();
        
        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
        
        // Caso 2: Diferentes nombres
        Role roleDifferent = Role.builder().nombre("ADMIN").build();
        
        assertNotEquals(role1, roleDifferent);
        assertNotEquals(role1.hashCode(), roleDifferent.hashCode());
        
        // Caso 3: Nombre null vs no null
        Role roleNullNombre = Role.builder().nombre(null).build();
        Role roleWithNombre = Role.builder().nombre("CLIENTE").build();
        
        assertNotEquals(roleNullNombre, roleWithNombre);
        assertNotEquals(roleWithNombre, roleNullNombre);
        
        // Caso 4: Ambos null
        Role role1Null = new Role();
        Role role2Null = new Role();
        
        assertEquals(role1Null, role2Null);
        assertEquals(role1Null.hashCode(), role2Null.hashCode());
        
        // Caso 5: canEqual con diferentes tipos
        assertFalse(role1.canEqual(null));
        assertFalse(role1.canEqual("string"));
        assertTrue(role1.canEqual(role2));
        
        // Caso 6: equals con null
        assertNotEquals(role1, null);
        
        // Caso 7: equals con mismo objeto
        assertEquals(role1, role1);
        
        // Caso 8: Diferentes nombres
        Role roleAdmin = Role.builder().nombre("ADMIN").build();
        assertNotEquals(role1, roleAdmin);
        
        // Caso 9: Diferentes IDs
        Role roleDifferentName = Role.builder().nombre("EMPLEADO").build();
        assertNotEquals(role1, roleDifferentName);
    }
}
