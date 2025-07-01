package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

class ProductoTest {

    private Producto producto;
    private Categoria categoria;
    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        categoria = Categoria.builder()
                .id(1L)
                .nombre("Categoría Test")
                .build();

        proveedor = Proveedor.builder()
                .id(1L)
                .nombre("Proveedor Test")
                .contacto("contacto@test.com")
                .build();

        producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción del producto")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();
    }

    @Test
    void testActualizarStockTrasReserva() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto A")
                .stock(10)
                .build();

        producto.actualizarStockTrasReserva(5);

        assertEquals(5, producto.getStock());
    }

    @Test
    void testActualizarStockTrasReservaExcedeStock() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto A")
                .stock(10)
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            producto.actualizarStockTrasReserva(15);
        });

        assertEquals("Cantidad reservada excede el stock disponible.", exception.getMessage());
    }

    @Test
    void testProductoBuilder() {
        assertNotNull(producto);
        assertEquals(1L, producto.getId());
        assertEquals("Producto Test", producto.getNombre());
        assertEquals("Descripción del producto", producto.getDescripcion());
        assertEquals(0, new BigDecimal("99.99").compareTo(producto.getPrecio()));
        assertEquals(100, producto.getStock());
        assertEquals(categoria, producto.getCategoria());
        assertEquals(proveedor, producto.getProveedor());
    }

    @Test
    void testActualizarStockTrasReserva_StockJusto() {
        int cantidadReservada = 100;

        producto.actualizarStockTrasReserva(cantidadReservada);

        assertEquals(0, producto.getStock());
    }

    @Test
    void testActualizarStockTrasReserva_CantidadCero() {
        int cantidadReservada = 0;

        producto.actualizarStockTrasReserva(cantidadReservada);

        assertEquals(100, producto.getStock());
    }

    @Test
    void testProductoConStockCero() {
        Producto productoSinStock = Producto.builder()
                .id(2L)
                .nombre("Producto Sin Stock")
                .stock(0)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            productoSinStock.actualizarStockTrasReserva(1);
        });
    }

    @Test
    public void testEquals() {
        Categoria categoria = Categoria.builder().id(1L).nombre("Test").build();
        Proveedor proveedor = Proveedor.builder().id(1L).nombre("Test").build();

        Producto producto1 = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();

        Producto producto2 = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();

        Producto producto3 = Producto.builder()
                .id(2L)
                .nombre("Producto Diferente")
                .descripcion("Otra descripción")
                .precio(new BigDecimal("50.00"))
                .stock(50)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();

        // Test equals - same objects
        assertEquals(producto1, producto1);

        // Test equals - equivalent objects
        assertEquals(producto1, producto2);

        // Test equals - different objects
        assertNotEquals(producto1, producto3);

        // Test equals - null
        assertNotEquals(producto1, null);

        // Test equals - different class
        assertNotEquals(producto1, "string");

        // Test canEqual method
        assertTrue(producto1.canEqual(producto2));
        assertFalse(producto1.canEqual("string"));
    }

    @Test
    public void testHashCode() {
        Categoria categoria = Categoria.builder().id(1L).nombre("Test").build();
        Proveedor proveedor = Proveedor.builder().id(1L).nombre("Test").build();

        Producto producto1 = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();

        Producto producto2 = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();

        // Test hashCode consistency
        assertEquals(producto1.hashCode(), producto2.hashCode());

        // Test hashCode multiple calls
        int hash1 = producto1.hashCode();
        int hash2 = producto1.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void testToString() {
        Categoria categoria = Categoria.builder().id(1L).nombre("Test").build();
        Proveedor proveedor = Proveedor.builder().id(1L).nombre("Test").build();

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();

        String toStringResult = producto.toString();

        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Producto"));
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("nombre=Producto Test"));
        assertTrue(toStringResult.contains("precio=99.99"));
        assertTrue(toStringResult.contains("stock=100"));
    }

    @Test
    public void testLombokSetters() {
        Producto producto = new Producto();
        Categoria categoria = Categoria.builder().id(1L).nombre("Test").build();
        Proveedor proveedor = Proveedor.builder().id(1L).nombre("Test").build();

        // Test all Lombok-generated setters
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción");
        producto.setPrecio(new BigDecimal("99.99"));
        producto.setStock(100);
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);

        // Verify setters worked
        assertEquals(1L, producto.getId());
        assertEquals("Producto Test", producto.getNombre());
        assertEquals("Descripción", producto.getDescripcion());
        assertEquals(new BigDecimal("99.99"), producto.getPrecio());
        assertEquals(100, producto.getStock());
        assertEquals(categoria, producto.getCategoria());
        assertEquals(proveedor, producto.getProveedor());
    }

    @Test
    public void testEqualsWithNullFields() {
        Producto producto1 = new Producto();
        producto1.setId(1L);

        Producto producto2 = new Producto();
        producto2.setId(1L);

        // Test equals with null fields
        assertEquals(producto1, producto2);

        producto1.setNombre("Test");
        assertNotEquals(producto1, producto2);

        producto2.setNombre("Test");
        assertEquals(producto1, producto2);
    }

    @Test
    public void testEqualsWithDifferentFields() {
        Categoria categoria1 = Categoria.builder().id(1L).build();
        Categoria categoria2 = Categoria.builder().id(2L).build();

        Producto producto1 = Producto.builder()
                .id(1L)
                .categoria(categoria1)
                .build();

        Producto producto2 = Producto.builder()
                .id(1L)
                .categoria(categoria2)
                .build();

        // Should be different because categorias are different
        assertNotEquals(producto1, producto2);
    }

    @Test
    public void testHashCodeWithDifferentFields() {
        Producto producto1 = Producto.builder()
                .id(1L)
                .nombre("Test")
                .precio(new BigDecimal("99.99"))
                .build();

        Producto producto2 = Producto.builder()
                .id(1L)
                .nombre("Test")
                .precio(new BigDecimal("99.99"))
                .build();

        // Test hashCode consistency
        assertEquals(producto1.hashCode(), producto2.hashCode());

        // Test with different values
        producto2.setNombre("Different");
        assertNotEquals(producto1.hashCode(), producto2.hashCode());
    }

    @Test
    public void testHashCodeWithNullFields() {
        Producto producto1 = new Producto();
        Producto producto2 = new Producto();

        // Test hashCode with null fields
        assertEquals(producto1.hashCode(), producto2.hashCode());

        // Test with one field set
        producto1.setNombre("Test");
        assertNotEquals(producto1.hashCode(), producto2.hashCode());

        // Test with same field set
        producto2.setNombre("Test");
        assertEquals(producto1.hashCode(), producto2.hashCode());
    }

    @Test
    public void testActualizarStockTrasReservaExcepcion() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .stock(5)
                .build();

        // Test que lanza excepción cuando la cantidad reservada excede el stock
        assertThrows(IllegalArgumentException.class, () -> {
            producto.actualizarStockTrasReserva(10);
        });

        // Verify that stock wasn't changed after exception
        assertEquals(5, producto.getStock());
    }

    @Test
    public void testActualizarStockTrasReservaCantidadCero() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .stock(10)
                .build();

        producto.actualizarStockTrasReserva(0);

        // Stock should remain the same
        assertEquals(10, producto.getStock());
    }

    @Test
    public void testActualizarStockTrasReservaCantidadCompleta() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .stock(10)
                .build();

        producto.actualizarStockTrasReserva(10);

        // Stock should be 0
        assertEquals(0, producto.getStock());
    }
    
    @Test
    void testProductoBuilderToString() {
        Producto.ProductoBuilder builder = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción del producto")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria)
                .proveedor(proveedor);
        
        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("ProductoBuilder"));
    }
    
    @Test
    public void testEqualsExhaustive() {
        // Test all combinations of fields to cover all branches
        Categoria categoria1 = Categoria.builder().id(1L).nombre("Cat1").build();
        Categoria categoria2 = Categoria.builder().id(2L).nombre("Cat2").build();
        Proveedor proveedor1 = Proveedor.builder().id(1L).nombre("Prov1").build();
        Proveedor proveedor2 = Proveedor.builder().id(2L).nombre("Prov2").build();
        
        // Base producto
        Producto base = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        
        // Test different id
        Producto differentId = Producto.builder()
                .id(2L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, differentId);
        
        // Test null id vs non-null id
        Producto nullId = Producto.builder()
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, nullId);
        assertNotEquals(nullId, base);
        
        // Test both null ids
        Producto nullId2 = Producto.builder()
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        assertEquals(nullId, nullId2);
        
        // Test different nombre
        Producto differentNombre = Producto.builder()
                .id(1L)
                .nombre("Producto Diferente")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, differentNombre);
        
        // Test null nombre vs non-null nombre
        Producto nullNombre = Producto.builder()
                .id(1L)
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, nullNombre);
        assertNotEquals(nullNombre, base);
        
        // Test different descripcion
        Producto differentDescripcion = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción Diferente")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, differentDescripcion);
        
        // Test null descripcion vs non-null descripcion
        Producto nullDescripcion = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, nullDescripcion);
        assertNotEquals(nullDescripcion, base);
        
        // Test different precio
        Producto differentPrecio = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("50.00"))
                .stock(100)
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, differentPrecio);
        
        // Test null precio vs non-null precio
        Producto nullPrecio = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .stock(100)
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, nullPrecio);
        assertNotEquals(nullPrecio, base);
        
        // Test different stock (Integer field)
        Producto differentStock = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(50)
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, differentStock);
        
        // Test null stock vs non-null stock
        Producto nullStock = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .categoria(categoria1)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, nullStock);
        assertNotEquals(nullStock, base);
        
        // Test different categoria
        Producto differentCategoria = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria2)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, differentCategoria);
        
        // Test null categoria vs non-null categoria
        Producto nullCategoria = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .proveedor(proveedor1)
                .build();
        assertNotEquals(base, nullCategoria);
        assertNotEquals(nullCategoria, base);
        
        // Test different proveedor
        Producto differentProveedor = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria1)
                .proveedor(proveedor2)
                .build();
        assertNotEquals(base, differentProveedor);
        
        // Test null proveedor vs non-null proveedor
        Producto nullProveedor = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria1)
                .build();
        assertNotEquals(base, nullProveedor);
        assertNotEquals(nullProveedor, base);
        
        // Test both null proveedores
        Producto nullProveedor2 = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria1)
                .build();
        assertEquals(nullProveedor, nullProveedor2);
    }
    
    @Test
    public void testHashCodeExhaustive() {
        // Test hashCode consistency with different field combinations
        Categoria categoria = Categoria.builder().id(1L).nombre("Test").build();
        Proveedor proveedor = Proveedor.builder().id(1L).nombre("Test").build();
        
        // Test hashCode with all fields
        Producto fullProducto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();
        
        // Test hashCode with null fields
        Producto nullFieldsProducto = new Producto();
        
        // Both should produce consistent hashCodes
        int fullHash1 = fullProducto.hashCode();
        int fullHash2 = fullProducto.hashCode();
        assertEquals(fullHash1, fullHash2);
        
        int nullHash1 = nullFieldsProducto.hashCode();
        int nullHash2 = nullFieldsProducto.hashCode();
        assertEquals(nullHash1, nullHash2);
        
        // Different objects with same values should have same hashCode
        Producto duplicate = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .descripcion("Descripción")
                .precio(new BigDecimal("99.99"))
                .stock(100)
                .categoria(categoria)
                .proveedor(proveedor)
                .build();
        
        assertEquals(fullProducto.hashCode(), duplicate.hashCode());
    }
    
    @Test
    public void testCanEqualExhaustive() {
        Producto producto = new Producto();
        
        // Test canEqual with same type
        assertTrue(producto.canEqual(new Producto()));
        
        // Test canEqual with different types
        assertFalse(producto.canEqual("string"));
        assertFalse(producto.canEqual(123));
        assertFalse(producto.canEqual(null));
        assertFalse(producto.canEqual(new Object()));
        
        // Test canEqual with subclass scenario
        Producto subProducto = new Producto() {};
        assertTrue(producto.canEqual(subProducto));
    }
}
