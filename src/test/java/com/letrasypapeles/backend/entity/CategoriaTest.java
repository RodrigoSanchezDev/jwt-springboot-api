package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoriaTest {

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = Categoria.builder()
                .id(1L)
                .nombre("Categoría Test")
                .build();
    }

    @Test
    void testCategoriaBuilder() {
        assertNotNull(categoria);
        assertEquals(1L, categoria.getId());
        assertEquals("Categoría Test", categoria.getNombre());
    }

    @Test
    void testConstructorVacio() {
        Categoria categoriaVacia = new Categoria();
        
        assertNotNull(categoriaVacia);
        assertNull(categoriaVacia.getId());
        assertNull(categoriaVacia.getNombre());
    }

    @Test
    void testSettersAndGetters() {
        Categoria cat = new Categoria();
        
        cat.setId(2L);
        cat.setNombre("Nueva Categoría");
        
        assertEquals(2L, cat.getId());
        assertEquals("Nueva Categoría", cat.getNombre());
    }

    @Test
    void testToString() {
        String toString = categoria.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Categoría Test"));
    }

    @Test
    void testEqualsAndHashCode() {
        Categoria categoria1 = Categoria.builder()
                .id(1L)
                .nombre("Test")
                .build();
                
        Categoria categoria2 = Categoria.builder()
                .id(1L)
                .nombre("Test")
                .build();
                
        assertEquals(categoria1, categoria2);
        assertEquals(categoria1.hashCode(), categoria2.hashCode());
    }

    @Test
    void testEqualsConMismoObjeto() {
        assertEquals(categoria, categoria);
    }

    @Test
    void testEqualsConNull() {
        assertNotEquals(categoria, null);
    }

    @Test
    void testEqualsConClaseDiferente() {
        assertNotEquals(categoria, "Una string");
        assertNotEquals(categoria, 123);
    }

    @Test
    void testEqualsConIdDiferente() {
        Categoria otraCategoria = Categoria.builder()
                .id(2L)
                .nombre("Categoría Test")
                .build();
        
        assertNotEquals(categoria, otraCategoria);
        assertNotEquals(categoria.hashCode(), otraCategoria.hashCode());
    }

    @Test
    void testEqualsConNombreDiferente() {
        Categoria otraCategoria = Categoria.builder()
                .id(1L)
                .nombre("Nombre Diferente")
                .build();
        
        assertNotEquals(categoria, otraCategoria);
    }

    @Test
    void testEqualsConIdNull() {
        Categoria categoria1 = Categoria.builder()
                .nombre("Test")
                .build();
                
        Categoria categoria2 = Categoria.builder()
                .nombre("Test")
                .build();
        
        assertEquals(categoria1, categoria2);
        
        Categoria categoria3 = Categoria.builder()
                .id(1L)
                .nombre("Test")
                .build();
        
        assertNotEquals(categoria1, categoria3);
    }

    @Test
    void testEqualsConNombreNull() {
        Categoria categoria1 = Categoria.builder()
                .id(1L)
                .build();
                
        Categoria categoria2 = Categoria.builder()
                .id(1L)
                .build();
        
        assertEquals(categoria1, categoria2);
        
        Categoria categoria3 = Categoria.builder()
                .id(1L)
                .nombre("Test")
                .build();
        
        assertNotEquals(categoria1, categoria3);
    }

    @Test
    void testCanEqual() {
        assertTrue(categoria.canEqual(new Categoria()));
        assertFalse(categoria.canEqual("Una string"));
        assertFalse(categoria.canEqual(null));
    }

    @Test
    void testHashCodeConsistencia() {
        int hashCode1 = categoria.hashCode();
        int hashCode2 = categoria.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeConValoresNull() {
        Categoria categoriaConNulls = new Categoria();
        int hashCode = categoriaConNulls.hashCode();
        // Solo verificamos que no lance excepción
        assertNotNull(hashCode);
    }

    @Test
    void testBuilderMetodos() {
        Categoria.CategoriaBuilder builder = Categoria.builder();
        assertNotNull(builder);
        
        // Test de todos los métodos del builder
        Categoria categoriaCompleta = builder
                .id(10L)
                .nombre("Categoria Builder Test")
                .build();
        
        assertEquals(10L, categoriaCompleta.getId());
        assertEquals("Categoria Builder Test", categoriaCompleta.getNombre());
    }

    @Test
    void testBuilderToString() {
        Categoria.CategoriaBuilder builder = Categoria.builder()
                .id(5L)
                .nombre("Test Builder");
        
        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("CategoriaBuilder"));
    }

    @Test
    void testValidarNombreObligatorio() {
        // Validar que el nombre sea obligatorio
        assertNotNull(categoria.getNombre());
        assertFalse(categoria.getNombre().isEmpty());
    }
}
