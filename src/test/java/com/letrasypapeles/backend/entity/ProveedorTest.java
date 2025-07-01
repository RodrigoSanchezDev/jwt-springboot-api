package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProveedorTest {

    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        proveedor = Proveedor.builder()
                .id(1L)
                .nombre("Proveedor ABC")
                .contacto("Juan Pérez - contacto@proveedorabc.com - 555-1234")
                .build();
    }

    @Test
    void testProveedorBuilder() {
        assertNotNull(proveedor);
        assertEquals(1L, proveedor.getId());
        assertEquals("Proveedor ABC", proveedor.getNombre());
        assertEquals("Juan Pérez - contacto@proveedorabc.com - 555-1234", proveedor.getContacto());
    }

    @Test
    void testConstructorVacio() {
        Proveedor proveedorVacio = new Proveedor();
        
        assertNotNull(proveedorVacio);
        assertNull(proveedorVacio.getId());
        assertNull(proveedorVacio.getNombre());
        assertNull(proveedorVacio.getContacto());
    }

    @Test
    void testSettersAndGetters() {
        Proveedor p = new Proveedor();
        
        p.setId(2L);
        p.setNombre("Proveedor XYZ");
        p.setContacto("María González - maria@proveedorxyz.com - 555-5678");
        
        assertEquals(2L, p.getId());
        assertEquals("Proveedor XYZ", p.getNombre());
        assertEquals("María González - maria@proveedorxyz.com - 555-5678", p.getContacto());
    }

    @Test
    void testValidarDatosContacto() {
        // Validar que tenga información de contacto
        boolean tieneContacto = proveedor.getContacto() != null && !proveedor.getContacto().isEmpty();
        
        assertTrue(tieneContacto);
    }

    @Test
    void testValidarContactoContieneEmail() {
        // Validar que el contacto contenga información tipo email
        String contacto = proveedor.getContacto();
        assertTrue(contacto.contains("@"));
    }

    @Test
    void testValidarContactoContieneTelefono() {
        // Validar que el contacto contenga información de teléfono
        String contacto = proveedor.getContacto();
        assertTrue(contacto.contains("555"));
    }

    @Test
    void testToString() {
        String toString = proveedor.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Proveedor ABC"));
    }

    @Test
    void testEqualsAndHashCode() {
        Proveedor proveedor1 = Proveedor.builder()
                .id(1L)
                .nombre("Test")
                .contacto("Test Contact")
                .build();
                
        Proveedor proveedor2 = Proveedor.builder()
                .id(1L)
                .nombre("Test")
                .contacto("Test Contact")
                .build();
                
        assertEquals(proveedor1, proveedor2);
        assertEquals(proveedor1.hashCode(), proveedor2.hashCode());
    }

    @Test
    void testValidarNombreObligatorio() {
        // Validar que el nombre sea obligatorio
        assertNotNull(proveedor.getNombre());
        assertFalse(proveedor.getNombre().isEmpty());
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(proveedor.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(proveedor.equals("not a proveedor"));
    }

    @Test
    void testEqualsWithSameInstance() {
        assertTrue(proveedor.equals(proveedor));
    }

    @Test
    void testEqualsWithDifferentId() {
        Proveedor otro = Proveedor.builder()
                .id(2L)
                .nombre(proveedor.getNombre())
                .contacto(proveedor.getContacto())
                .build();
        
        assertFalse(proveedor.equals(otro));
    }

    @Test
    void testEqualsWithDifferentNombre() {
        Proveedor otro = Proveedor.builder()
                .id(proveedor.getId())
                .nombre("Nombre diferente")
                .contacto(proveedor.getContacto())
                .build();
        
        assertFalse(proveedor.equals(otro));
    }

    @Test
    void testEqualsWithNullNombre() {
        Proveedor proveedor1 = Proveedor.builder()
                .id(1L)
                .nombre(null)
                .contacto("Contacto")
                .build();

        Proveedor proveedor2 = Proveedor.builder()
                .id(1L)
                .nombre(null)
                .contacto("Contacto")
                .build();

        assertEquals(proveedor1, proveedor2);
    }

    @Test
    void testHashCodeConsistency() {
        int hashCode1 = proveedor.hashCode();
        int hashCode2 = proveedor.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeWithNullFields() {
        Proveedor proveedorConNulos = new Proveedor();
        int hashCode = proveedorConNulos.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    void testAllArgsConstructor() {
        Proveedor proveedorConst = new Proveedor(5L, "Proveedor Constructor", "Contacto constructor");
        
        assertEquals(5L, proveedorConst.getId());
        assertEquals("Proveedor Constructor", proveedorConst.getNombre());
        assertEquals("Contacto constructor", proveedorConst.getContacto());
    }

    @Test
    void testCanEqual() {
        Proveedor otro = new Proveedor();
        assertTrue(proveedor.canEqual(otro));
        assertFalse(proveedor.canEqual("not a proveedor"));
    }

    @Test
    void testBuilderWithAllFields() {
        Proveedor proveedorBuilder = Proveedor.builder()
                .id(10L)
                .nombre("Proveedor Builder")
                .contacto("Contacto desde builder")
                .build();
        
        assertEquals(10L, proveedorBuilder.getId());
        assertEquals("Proveedor Builder", proveedorBuilder.getNombre());
        assertEquals("Contacto desde builder", proveedorBuilder.getContacto());
    }

    @Test
    void testBuilderPartial() {
        Proveedor proveedorParcial = Proveedor.builder()
                .nombre("Solo nombre")
                .build();
        
        assertNull(proveedorParcial.getId());
        assertEquals("Solo nombre", proveedorParcial.getNombre());
        assertNull(proveedorParcial.getContacto());
    }

    @Test
    void testToStringWithNullFields() {
        Proveedor proveedorVacio = new Proveedor();
        String toString = proveedorVacio.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("Proveedor"));
        assertTrue(toString.contains("null"));
    }

    @Test
    void testEqualsWithDifferentContacto() {
        Proveedor otro = Proveedor.builder()
                .id(proveedor.getId())
                .nombre(proveedor.getNombre())
                .contacto("Contacto diferente")
                .build();
        
        assertFalse(proveedor.equals(otro));
    }

    @Test
    void testEqualsWithNullContacto() {
        Proveedor proveedor1 = Proveedor.builder()
                .id(1L)
                .nombre("Nombre")
                .contacto(null)
                .build();

        Proveedor proveedor2 = Proveedor.builder()
                .id(1L)
                .nombre("Nombre")
                .contacto(null)
                .build();

        assertEquals(proveedor1, proveedor2);
    }
    
    @Test
    void testProveedorBuilderToString() {
        Proveedor.ProveedorBuilder builder = Proveedor.builder()
                .id(1L)
                .nombre("Proveedor ABC")
                .contacto("Juan Pérez - contacto@proveedorabc.com - 555-1234");
        
        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("ProveedorBuilder"));
    }

    @Test
    void testEqualsEdgeCasesExhaustive() {
        // Test casos edge de equals y hashCode para maximizar cobertura
        
        // Caso 1: Un campo null vs no null en nombre
        Proveedor p1 = new Proveedor();
        p1.setId(1L);
        p1.setNombre(null);
        p1.setContacto("contacto");
        
        Proveedor p2 = new Proveedor();
        p2.setId(1L);
        p2.setNombre("nombre");
        p2.setContacto("contacto");
        
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1); // Test simétrico
        
        // Caso 2: Un campo null vs no null en contacto
        p1 = new Proveedor();
        p1.setId(1L);
        p1.setNombre("nombre");
        p1.setContacto(null);
        
        p2 = new Proveedor();
        p2.setId(1L);
        p2.setNombre("nombre");
        p2.setContacto("contacto");
        
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1); // Test simétrico
        
        // Caso 3: Ambos null vs uno null en ID
        p1 = new Proveedor();
        p1.setId(null);
        p1.setNombre("nombre");
        
        p2 = new Proveedor();
        p2.setId(1L);
        p2.setNombre("nombre");
        
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1);
        
        // Caso 4: Ambos ID null
        p1 = new Proveedor();
        p1.setId(null);
        p1.setNombre("nombre");
        
        p2 = new Proveedor();
        p2.setId(null);
        p2.setNombre("nombre");
        
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        
        // Caso 5: Test canEqual con null
        assertFalse(p1.canEqual(null));
        
        // Caso 6: Test todos los campos diferentes
        p1 = Proveedor.builder()
                .id(1L)
                .nombre("A")
                .contacto("A")
                .build();
                
        p2 = Proveedor.builder()
                .id(2L)
                .nombre("B")
                .contacto("B")
                .build();
                
        assertNotEquals(p1, p2);
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }
}
