package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SucursalTest {

    private Sucursal sucursal;

    @BeforeEach
    void setUp() {
        sucursal = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Centro")
                .direccion("Av. Principal 123")
                .region("Metropolitana")
                .build();
    }

    @Test
    void testSucursalBuilder() {
        assertNotNull(sucursal);
        assertEquals(1L, sucursal.getId());
        assertEquals("Sucursal Centro", sucursal.getNombre());
        assertEquals("Av. Principal 123", sucursal.getDireccion());
        assertEquals("Metropolitana", sucursal.getRegion());
    }

    @Test
    void testConstructorVacio() {
        Sucursal sucursalVacia = new Sucursal();
        
        assertNotNull(sucursalVacia);
        assertNull(sucursalVacia.getId());
        assertNull(sucursalVacia.getNombre());
        assertNull(sucursalVacia.getDireccion());
        assertNull(sucursalVacia.getRegion());
    }

    @Test
    void testSettersAndGetters() {
        Sucursal s = new Sucursal();
        
        s.setId(2L);
        s.setNombre("Sucursal Norte");
        s.setDireccion("Calle Norte 456");
        s.setRegion("Norte");
        
        assertEquals(2L, s.getId());
        assertEquals("Sucursal Norte", s.getNombre());
        assertEquals("Calle Norte 456", s.getDireccion());
        assertEquals("Norte", s.getRegion());
    }

    @Test
    void testValidarDatosCompletos() {
        // Validar que tenga nombre y direccion
        boolean tieneInformacionCompleta = sucursal.getNombre() != null && !sucursal.getNombre().isEmpty() &&
                                          sucursal.getDireccion() != null && !sucursal.getDireccion().isEmpty();
        
        assertTrue(tieneInformacionCompleta);
    }

    @Test
    void testValidarRegion() {
        // Validar que la región esté definida
        assertNotNull(sucursal.getRegion());
        assertFalse(sucursal.getRegion().isEmpty());
    }

    @Test
    void testToString() {
        String toString = sucursal.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Sucursal Centro"));
    }

    @Test
    void testEqualsAndHashCode() {
        Sucursal sucursal1 = Sucursal.builder()
                .id(1L)
                .nombre("Test")
                .direccion("Test Dir")
                .region("Test Region")
                .build();
                
        Sucursal sucursal2 = Sucursal.builder()
                .id(1L)
                .nombre("Test")
                .direccion("Test Dir")
                .region("Test Region")
                .build();
                
        assertEquals(sucursal1, sucursal2);
        assertEquals(sucursal1.hashCode(), sucursal2.hashCode());
    }

    @Test
    void testEqualsConMismoObjeto() {
        assertEquals(sucursal, sucursal);
    }

    @Test
    void testEqualsConNull() {
        assertNotEquals(sucursal, null);
    }

    @Test
    void testEqualsConClaseDiferente() {
        assertNotEquals(sucursal, "Una string");
        assertNotEquals(sucursal, 123);
    }

    @Test
    void testEqualsConIdDiferente() {
        Sucursal otraSucursal = Sucursal.builder()
                .id(2L)
                .nombre("Sucursal Centro")
                .direccion("Av. Principal 123")
                .region("Metropolitana")
                .build();
        
        assertNotEquals(sucursal, otraSucursal);
        assertNotEquals(sucursal.hashCode(), otraSucursal.hashCode());
    }

    @Test
    void testEqualsConNombreDiferente() {
        Sucursal otraSucursal = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Norte")
                .direccion("Av. Principal 123")
                .region("Metropolitana")
                .build();
        
        assertNotEquals(sucursal, otraSucursal);
    }

    @Test
    void testEqualsConDireccionDiferente() {
        Sucursal otraSucursal = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Centro")
                .direccion("Otra Direccion")
                .region("Metropolitana")
                .build();
        
        assertNotEquals(sucursal, otraSucursal);
    }

    @Test
    void testEqualsConRegionDiferente() {
        Sucursal otraSucursal = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Centro")
                .direccion("Av. Principal 123")
                .region("Norte")
                .build();
        
        assertNotEquals(sucursal, otraSucursal);
    }

    @Test
    void testEqualsConIdNull() {
        Sucursal sucursal1 = Sucursal.builder()
                .nombre("Test")
                .direccion("Test Dir")
                .region("Test Region")
                .build();
                
        Sucursal sucursal2 = Sucursal.builder()
                .nombre("Test")
                .direccion("Test Dir")
                .region("Test Region")
                .build();
        
        assertEquals(sucursal1, sucursal2);
        
        Sucursal sucursal3 = Sucursal.builder()
                .id(1L)
                .nombre("Test")
                .direccion("Test Dir")
                .region("Test Region")
                .build();
        
        assertNotEquals(sucursal1, sucursal3);
    }

    @Test
    void testEqualsConNombreNull() {
        Sucursal sucursal1 = Sucursal.builder()
                .id(1L)
                .direccion("Test Dir")
                .region("Test Region")
                .build();
                
        Sucursal sucursal2 = Sucursal.builder()
                .id(1L)
                .direccion("Test Dir")
                .region("Test Region")
                .build();
        
        assertEquals(sucursal1, sucursal2);
        
        Sucursal sucursal3 = Sucursal.builder()
                .id(1L)
                .nombre("Test")
                .direccion("Test Dir")
                .region("Test Region")
                .build();
        
        assertNotEquals(sucursal1, sucursal3);
    }

    @Test
    void testEqualsConDireccionNull() {
        Sucursal sucursal1 = Sucursal.builder()
                .id(1L)
                .nombre("Test")
                .region("Test Region")
                .build();
                
        Sucursal sucursal2 = Sucursal.builder()
                .id(1L)
                .nombre("Test")
                .region("Test Region")
                .build();
        
        assertEquals(sucursal1, sucursal2);
        
        Sucursal sucursal3 = Sucursal.builder()
                .id(1L)
                .nombre("Test")
                .direccion("Test Dir")
                .region("Test Region")
                .build();
        
        assertNotEquals(sucursal1, sucursal3);
    }

    @Test
    void testEqualsConRegionNull() {
        Sucursal sucursal1 = Sucursal.builder()
                .id(1L)
                .nombre("Test")
                .direccion("Test Dir")
                .build();
                
        Sucursal sucursal2 = Sucursal.builder()
                .id(1L)
                .nombre("Test")
                .direccion("Test Dir")
                .build();
        
        assertEquals(sucursal1, sucursal2);
        
        Sucursal sucursal3 = Sucursal.builder()
                .id(1L)
                .nombre("Test")
                .direccion("Test Dir")
                .region("Test Region")
                .build();
        
        assertNotEquals(sucursal1, sucursal3);
    }

    @Test
    void testCanEqual() {
        assertTrue(sucursal.canEqual(new Sucursal()));
        assertFalse(sucursal.canEqual("Una string"));
        assertFalse(sucursal.canEqual(null));
    }

    @Test
    void testHashCodeConsistencia() {
        int hashCode1 = sucursal.hashCode();
        int hashCode2 = sucursal.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeConValoresNull() {
        Sucursal sucursalConNulls = new Sucursal();
        int hashCode = sucursalConNulls.hashCode();
        // Solo verificamos que no lance excepción
        assertNotNull(hashCode);
    }

    @Test
    void testBuilderMetodos() {
        Sucursal.SucursalBuilder builder = Sucursal.builder();
        assertNotNull(builder);
        
        // Test de todos los métodos del builder
        Sucursal sucursalCompleta = builder
                .id(10L)
                .nombre("Sucursal Test")
                .direccion("Direccion Test")
                .region("Region Test")
                .build();
        
        assertEquals(10L, sucursalCompleta.getId());
        assertEquals("Sucursal Test", sucursalCompleta.getNombre());
        assertEquals("Direccion Test", sucursalCompleta.getDireccion());
        assertEquals("Region Test", sucursalCompleta.getRegion());
    }

    @Test
    void testBuilderToString() {
        Sucursal.SucursalBuilder builder = Sucursal.builder()
                .id(5L)
                .nombre("Test Builder");
        
        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("SucursalBuilder"));
    }
}
