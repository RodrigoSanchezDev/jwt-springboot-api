package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventarioTest {

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        inventario = new Inventario();
    }

    @Test
    public void testControlDeUmbrales() {
        Inventario inventario = new Inventario(4, 5);

        System.out.println("Test: Control de Umbrales");
        System.out.println("Stock actual inicial: " + inventario.getStockActual());
        System.out.println("Umbral mínimo inicial: " + inventario.getUmbralMinimo());

        boolean resultado = inventario.necesitaReabastecimiento();
        System.out.println("Resultado de necesitaReabastecimiento: " + resultado);

        assertTrue(resultado);
    }

    @Test
    public void testNoNecesitaReabastecimiento() {
        Inventario inventario = new Inventario();
        inventario.setStockActual(20);
        inventario.setUmbralMinimo(5);

        assertFalse(inventario.necesitaReabastecimiento());
    }

    @Test
    void testConstructorConParametros() {
        Inventario inventarioConParametros = new Inventario(50, 10);

        assertEquals(50, inventarioConParametros.getStockActual());
        assertEquals(10, inventarioConParametros.getUmbralMinimo());
    }

    @Test
    void testSetStockActual() {
        inventario.setStockActual(25);

        assertEquals(25, inventario.getStockActual());
    }

    @Test
    void testGetStockActual() {
        inventario.setStockActual(75);

        int stockActual = inventario.getStockActual();

        assertEquals(75, stockActual);
    }

    @Test
    void testSetUmbralMinimo() {
        inventario.setUmbralMinimo(15);

        assertEquals(15, inventario.getUmbralMinimo());
    }

    @Test
    void testGetUmbralMinimo() {
        inventario.setUmbralMinimo(20);

        int umbralMinimo = inventario.getUmbralMinimo();

        assertEquals(20, umbralMinimo);
    }

    @Test
    void testNecesitaReabastecimiento_Igual() {
        inventario.setStockActual(10);
        inventario.setUmbralMinimo(10);

        boolean necesitaReabastecimiento = inventario.necesitaReabastecimiento();

        assertFalse(necesitaReabastecimiento); // Igual no necesita reabastecimiento
    }

    @Test
    void testBuilder() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .build();

        Sucursal sucursal = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Test")
                .build();

        Inventario inventarioBuilder = Inventario.builder()
                .id(1L)
                .cantidad(100)
                .umbral(20)
                .producto(producto)
                .sucursal(sucursal)
                .stockActual(100)
                .umbralMinimo(20)
                .build();

        assertNotNull(inventarioBuilder);
        assertEquals(1L, inventarioBuilder.getId());
        assertEquals(100, inventarioBuilder.getCantidad());
        assertEquals(20, inventarioBuilder.getUmbral());
        assertEquals(producto, inventarioBuilder.getProducto());
        assertEquals(sucursal, inventarioBuilder.getSucursal());
        assertEquals(100, inventarioBuilder.getStockActual());
        assertEquals(20, inventarioBuilder.getUmbralMinimo());
    }

    @Test
    public void testEquals() {
        Producto producto = new Producto();
        producto.setId(1L);
        
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        
        Inventario inventario1 = Inventario.builder()
                .id(1L)
                .cantidad(100)
                .umbral(20)
                .producto(producto)
                .sucursal(sucursal)
                .stockActual(50)
                .umbralMinimo(10)
                .build();
        
        Inventario inventario2 = Inventario.builder()
                .id(1L)
                .cantidad(100)
                .umbral(20)
                .producto(producto)
                .sucursal(sucursal)
                .stockActual(50)
                .umbralMinimo(10)
                .build();
        
        Inventario inventario3 = Inventario.builder()
                .id(2L)
                .cantidad(200)
                .umbral(30)
                .producto(producto)
                .sucursal(sucursal)
                .stockActual(60)
                .umbralMinimo(15)
                .build();
        
        // Test equals - same objects
        assertEquals(inventario1, inventario1);
        
        // Test equals - equivalent objects
        assertEquals(inventario1, inventario2);
        
        // Test equals - different objects
        assertNotEquals(inventario1, inventario3);
        
        // Test equals - null
        assertNotEquals(inventario1, null);
        
        // Test equals - different class
        assertNotEquals(inventario1, "string");
        
        // Test canEqual method
        assertTrue(inventario1.canEqual(inventario2));
        assertFalse(inventario1.canEqual("string"));
    }

    @Test
    public void testHashCode() {
        Producto producto = new Producto();
        producto.setId(1L);
        
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        
        Inventario inventario1 = Inventario.builder()
                .id(1L)
                .cantidad(100)
                .umbral(20)
                .producto(producto)
                .sucursal(sucursal)
                .stockActual(50)
                .umbralMinimo(10)
                .build();
        
        Inventario inventario2 = Inventario.builder()
                .id(1L)
                .cantidad(100)
                .umbral(20)
                .producto(producto)
                .sucursal(sucursal)
                .stockActual(50)
                .umbralMinimo(10)
                .build();
        
        // Test hashCode consistency
        assertEquals(inventario1.hashCode(), inventario2.hashCode());
        
        // Test hashCode multiple calls
        int hash1 = inventario1.hashCode();
        int hash2 = inventario1.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void testToString() {
        Producto producto = new Producto();
        producto.setId(1L);
        
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        
        Inventario inventario = Inventario.builder()
                .id(1L)
                .cantidad(100)
                .umbral(20)
                .producto(producto)
                .sucursal(sucursal)
                .stockActual(50)
                .umbralMinimo(10)
                .build();
        
        String toStringResult = inventario.toString();
        
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Inventario"));
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("cantidad=100"));
        assertTrue(toStringResult.contains("umbral=20"));
        assertTrue(toStringResult.contains("stockActual=50"));
        assertTrue(toStringResult.contains("umbralMinimo=10"));
    }

    @Test
    public void testLombokSetters() {
        Inventario inventario = new Inventario();
        Producto producto = new Producto();
        producto.setId(1L);
        
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        
        // Test all Lombok-generated setters
        inventario.setId(1L);
        inventario.setCantidad(100);
        inventario.setUmbral(20);
        inventario.setProducto(producto);
        inventario.setSucursal(sucursal);
        
        // Verify setters worked
        assertEquals(1L, inventario.getId());
        assertEquals(100, inventario.getCantidad());
        assertEquals(20, inventario.getUmbral());
        assertEquals(producto, inventario.getProducto());
        assertEquals(sucursal, inventario.getSucursal());
    }

    @Test
    public void testBuilderPattern() {
        Producto producto = new Producto();
        producto.setId(1L);
        
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        
        // Test builder pattern
        Inventario inventario = Inventario.builder()
                .id(1L)
                .cantidad(100)
                .umbral(20)
                .producto(producto)
                .sucursal(sucursal)
                .stockActual(50)
                .umbralMinimo(10)
                .build();
        
        assertNotNull(inventario);
        assertEquals(1L, inventario.getId());
        assertEquals(100, inventario.getCantidad());
        assertEquals(20, inventario.getUmbral());
        assertEquals(producto, inventario.getProducto());
        assertEquals(sucursal, inventario.getSucursal());
        assertEquals(50, inventario.getStockActual());
        assertEquals(10, inventario.getUmbralMinimo());
    }

    @Test
    public void testEqualsWithNullFields() {
        Inventario inventario1 = new Inventario();
        inventario1.setId(1L);
        
        Inventario inventario2 = new Inventario();
        inventario2.setId(1L);
        
        // Test equals with null fields
        assertEquals(inventario1, inventario2);
        
        inventario1.setCantidad(100);
        assertNotEquals(inventario1, inventario2);
        
        inventario2.setCantidad(100);
        assertEquals(inventario1, inventario2);
    }

    @Test
    public void testEqualsWithDifferentFields() {
        Producto producto1 = new Producto();
        producto1.setId(1L);
        
        Producto producto2 = new Producto();
        producto2.setId(2L);
        
        Inventario inventario1 = Inventario.builder()
                .id(1L)
                .producto(producto1)
                .build();
        
        Inventario inventario2 = Inventario.builder()
                .id(1L)
                .producto(producto2)
                .build();
        
        // Should be different because productos are diferentes
        assertNotEquals(inventario1, inventario2);
    }
    
    @Test
    void testInventarioBuilderToString() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        
        Sucursal sucursal = new Sucursal();
        sucursal.setId(1L);
        sucursal.setNombre("Sucursal Test");
        
        Inventario.InventarioBuilder builder = Inventario.builder()
                .id(1L)
                .cantidad(100)
                .umbral(10)
                .stockActual(50)
                .umbralMinimo(5)
                .producto(producto)
                .sucursal(sucursal);
        
        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("InventarioBuilder"));
    }
}
