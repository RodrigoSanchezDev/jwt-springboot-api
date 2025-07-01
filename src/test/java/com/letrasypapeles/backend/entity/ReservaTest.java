package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class ReservaTest {

    private Reserva reserva;
    private Cliente cliente;
    private Producto producto;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1L)
                .nombre("Cliente Test")
                .email("cliente@test.com")
                .build();

        producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .precio(new BigDecimal("50.00"))
                .stock(20)
                .build();

        reserva = Reserva.builder()
                .id(1L)
                .fechaReserva(LocalDateTime.now())
                .estado("Pendiente")
                .cantidad(5)
                .cliente(cliente)
                .producto(producto)
                .build();
    }

    @Test
    public void testPermitirReservaSiHayStock() {
        Producto producto = new Producto();
        producto.setStock(20);

        Reserva reserva = new Reserva();
        reserva.setProducto(producto);
        reserva.setCantidad(10);

        assertTrue(reserva.permitirReserva());
    }

    @Test
    public void testNoPermitirReservaSiNoHayStock() {
        Producto producto = new Producto();
        producto.setStock(5);

        Reserva reserva = new Reserva();
        reserva.setProducto(producto);
        reserva.setCantidad(10);

        assertFalse(reserva.permitirReserva());
    }

    @Test
    void testReservaBuilder() {
        assertNotNull(reserva);
        assertEquals(1L, reserva.getId());
        assertEquals("Pendiente", reserva.getEstado());
        assertEquals(5, reserva.getCantidad());
        assertEquals(cliente, reserva.getCliente());
        assertEquals(producto, reserva.getProducto());
        assertNotNull(reserva.getFechaReserva());
    }

    @Test
    void testSetCantidad() {
        reserva.setCantidad(10);

        assertEquals(10, reserva.getCantidad());
    }

    @Test
    void testGetCantidad() {
        int cantidad = reserva.getCantidad();

        assertEquals(5, cantidad);
    }

    @Test
    void testPermitirReserva_StockJusto() {
        reserva.setCantidad(20); // Cantidad igual al stock

        boolean permitir = reserva.permitirReserva();

        assertTrue(permitir);
    }

    @Test
    void testPermitirReserva_CantidadCero() {
        reserva.setCantidad(0);

        boolean permitir = reserva.permitirReserva();

        assertTrue(permitir);
    }

    @Test
    void testConstructorVacio() {
        Reserva reservaVacia = new Reserva();

        assertNotNull(reservaVacia);
        assertNull(reservaVacia.getId());
        assertNull(reservaVacia.getEstado());
        assertEquals(0, reservaVacia.getCantidad());
    }

    @Test
    void testModificarEstado() {
        reserva.setEstado("Confirmada");

        assertEquals("Confirmada", reserva.getEstado());
    }

    @Test
    public void testEquals() {
        Cliente cliente = Cliente.builder().id(1L).build();
        Producto producto = Producto.builder().id(1L).build();
        LocalDateTime fecha = LocalDateTime.now();

        Reserva reserva1 = Reserva.builder()
                .id(1L)
                .fechaReserva(fecha)
                .estado("Pendiente")
                .cantidad(5)
                .cliente(cliente)
                .producto(producto)
                .build();

        Reserva reserva2 = Reserva.builder()
                .id(1L)
                .fechaReserva(fecha)
                .estado("Pendiente")
                .cantidad(5)
                .cliente(cliente)
                .producto(producto)
                .build();

        Reserva reserva3 = Reserva.builder()
                .id(2L)
                .fechaReserva(fecha)
                .estado("Confirmada")
                .cantidad(10)
                .cliente(cliente)
                .producto(producto)
                .build();

        // Test equals - same objects
        assertEquals(reserva1, reserva1);

        // Test equals - equivalent objects
        assertEquals(reserva1, reserva2);

        // Test equals - different objects
        assertNotEquals(reserva1, reserva3);

        // Test equals - null
        assertNotEquals(reserva1, null);

        // Test equals - different class
        assertNotEquals(reserva1, "string");

        // Test canEqual method
        assertTrue(reserva1.canEqual(reserva2));
        assertFalse(reserva1.canEqual("string"));
    }

    @Test
    public void testHashCode() {
        Cliente cliente = Cliente.builder().id(1L).build();
        Producto producto = Producto.builder().id(1L).build();
        LocalDateTime fecha = LocalDateTime.now();

        Reserva reserva1 = Reserva.builder()
                .id(1L)
                .fechaReserva(fecha)
                .estado("Pendiente")
                .cantidad(5)
                .cliente(cliente)
                .producto(producto)
                .build();

        Reserva reserva2 = Reserva.builder()
                .id(1L)
                .fechaReserva(fecha)
                .estado("Pendiente")
                .cantidad(5)
                .cliente(cliente)
                .producto(producto)
                .build();

        // Test hashCode consistency
        assertEquals(reserva1.hashCode(), reserva2.hashCode());

        // Test hashCode multiple calls
        int hash1 = reserva1.hashCode();
        int hash2 = reserva1.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void testToString() {
        Cliente cliente = Cliente.builder().id(1L).build();
        Producto producto = Producto.builder().id(1L).build();
        LocalDateTime fecha = LocalDateTime.now();

        Reserva reserva = Reserva.builder()
                .id(1L)
                .fechaReserva(fecha)
                .estado("Pendiente")
                .cantidad(5)
                .cliente(cliente)
                .producto(producto)
                .build();

        String toStringResult = reserva.toString();

        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Reserva"));
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("estado=Pendiente"));
        assertTrue(toStringResult.contains("cantidad=5"));
    }

    @Test
    public void testLombokSetters() {
        Reserva reserva = new Reserva();
        Cliente cliente = Cliente.builder().id(1L).build();
        Producto producto = Producto.builder().id(1L).build();
        LocalDateTime fecha = LocalDateTime.now();

        // Test all Lombok-generated setters
        reserva.setId(1L);
        reserva.setFechaReserva(fecha);
        reserva.setEstado("Pendiente");
        reserva.setCliente(cliente);
        reserva.setProducto(producto);

        // Verify setters worked
        assertEquals(1L, reserva.getId());
        assertEquals(fecha, reserva.getFechaReserva());
        assertEquals("Pendiente", reserva.getEstado());
        assertEquals(cliente, reserva.getCliente());
        assertEquals(producto, reserva.getProducto());
    }

    @Test
    public void testEqualsWithNullFields() {
        Reserva reserva1 = new Reserva();
        reserva1.setId(1L);

        Reserva reserva2 = new Reserva();
        reserva2.setId(1L);

        // Test equals with null fields
        assertEquals(reserva1, reserva2);

        reserva1.setEstado("Pendiente");
        assertNotEquals(reserva1, reserva2);

        reserva2.setEstado("Pendiente");
        assertEquals(reserva1, reserva2);
    }

    @Test
    public void testEqualsWithDifferentClients() {
        Cliente cliente1 = Cliente.builder().id(1L).build();
        Cliente cliente2 = Cliente.builder().id(2L).build();

        Reserva reserva1 = Reserva.builder()
                .id(1L)
                .cliente(cliente1)
                .build();

        Reserva reserva2 = Reserva.builder()
                .id(1L)
                .cliente(cliente2)
                .build();

        // Should be different because clientes are different
        assertNotEquals(reserva1, reserva2);
    }
    
    @Test
    void testReservaBuilderToString() {
        Reserva.ReservaBuilder builder = Reserva.builder()
                .id(1L)
                .fechaReserva(LocalDateTime.now())
                .estado("Pendiente")
                .cantidad(5)
                .cliente(cliente)
                .producto(producto);
        
        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("ReservaBuilder"));
    }
}
