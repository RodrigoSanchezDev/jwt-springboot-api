package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class PedidoTest {

    private Pedido pedido;
    private Cliente cliente;
    private Producto producto;
    private List<Producto> listaProductos;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .email("juan@test.com")
                .build();

        producto = Producto.builder()
                .id(1L)
                .nombre("Producto Test")
                .precio(new BigDecimal("100.0"))
                .build();

        listaProductos = new ArrayList<>();
        listaProductos.add(producto);

        pedido = Pedido.builder()
                .id(1L)
                .cliente(cliente)
                .listaProductos(listaProductos)
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .build();
    }

    @Test
    void testPedidoBuilder() {
        assertNotNull(pedido);
        assertEquals(1L, pedido.getId());
        assertEquals(cliente, pedido.getCliente());
        assertEquals(listaProductos, pedido.getListaProductos());
        assertEquals("PENDIENTE", pedido.getEstado());
        assertNotNull(pedido.getFecha());
    }

    @Test
    void testConstructorVacio() {
        Pedido pedidoVacio = new Pedido();
        
        assertNotNull(pedidoVacio);
        assertNull(pedidoVacio.getId());
        assertNull(pedidoVacio.getCliente());
        assertNull(pedidoVacio.getListaProductos());
        assertNull(pedidoVacio.getEstado());
        assertNull(pedidoVacio.getFecha());
    }

    @Test
    void testCalcularPrecioTotalProductos() {
        // Método de lógica de negocio para calcular precio total de productos
        BigDecimal precioTotal = BigDecimal.ZERO;
        for (Producto p : listaProductos) {
            precioTotal = precioTotal.add(p.getPrecio());
        }
        
        assertEquals(new BigDecimal("100.0"), precioTotal);
    }

    @Test
    void testActualizarEstado() {
        pedido.setEstado("PROCESADO");
        assertEquals("PROCESADO", pedido.getEstado());
        
        pedido.setEstado("ENVIADO");
        assertEquals("ENVIADO", pedido.getEstado());
        
        pedido.setEstado("ENTREGADO");
        assertEquals("ENTREGADO", pedido.getEstado());
    }

    @Test
    void testAgregarProductoALista() {
        // Crear nuevo producto
        Producto producto2 = Producto.builder()
                .id(2L)
                .nombre("Producto 2")
                .precio(new BigDecimal("50.0"))
                .build();
        
        // Agregar a la lista
        pedido.getListaProductos().add(producto2);
        
        assertEquals(2, pedido.getListaProductos().size());
        assertTrue(pedido.getListaProductos().contains(producto2));
    }

    @Test
    void testSettersAndGetters() {
        Pedido p = new Pedido();
        LocalDateTime fecha = LocalDateTime.now();
        List<Producto> nuevaLista = new ArrayList<>();
        nuevaLista.add(producto);
        
        p.setId(2L);
        p.setCliente(cliente);
        p.setListaProductos(nuevaLista);
        p.setFecha(fecha);
        p.setEstado("COMPLETADO");
        
        assertEquals(2L, p.getId());
        assertEquals(cliente, p.getCliente());
        assertEquals(nuevaLista, p.getListaProductos());
        assertEquals(fecha, p.getFecha());
        assertEquals("COMPLETADO", p.getEstado());
    }

    @Test
    void testContarProductosEnPedido() {
        // Método de lógica de negocio para contar productos
        int cantidadProductos = pedido.getListaProductos().size();
        assertEquals(1, cantidadProductos);
        
        // Agregar más productos
        Producto producto2 = Producto.builder()
                .id(2L)
                .nombre("Producto 2")
                .precio(new BigDecimal("25.0"))
                .build();
        
        pedido.getListaProductos().add(producto2);
        cantidadProductos = pedido.getListaProductos().size();
        assertEquals(2, cantidadProductos);
    }

    @Test
    void testPedidoEqualsAndHashCode() {
        Pedido pedido1 = Pedido.builder()
                .id(1L)
                .cliente(cliente)
                .listaProductos(listaProductos)
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE")
                .build();

        Pedido pedido2 = Pedido.builder()
                .id(1L)
                .cliente(cliente)
                .listaProductos(listaProductos)
                .fecha(pedido1.getFecha())
                .estado("PENDIENTE")
                .build();

        assertEquals(pedido1, pedido2);
        assertEquals(pedido1.hashCode(), pedido2.hashCode());
    }

    @Test
    void testPedidoEqualsExhaustive() {
        // Test equals con el mismo objeto
        assertEquals(pedido, pedido);
        
        // Test equals con null
        assertNotEquals(pedido, null);
        
        // Test equals con objeto de diferente clase
        assertNotEquals(pedido, "string");
        
        // Test equals con diferentes IDs
        Pedido pedidoDiferenteId = Pedido.builder()
                .id(2L)
                .cliente(cliente)
                .listaProductos(listaProductos)
                .fecha(pedido.getFecha())
                .estado("PENDIENTE")
                .build();
        assertNotEquals(pedido, pedidoDiferenteId);
        
        // Test equals con diferente fecha
        Pedido pedidoDiferenteFecha = Pedido.builder()
                .id(1L)
                .cliente(cliente)
                .listaProductos(listaProductos)
                .fecha(LocalDateTime.now().plusDays(1))
                .estado("PENDIENTE")
                .build();
        assertNotEquals(pedido, pedidoDiferenteFecha);
        
        // Test equals con diferente estado
        Pedido pedidoDiferenteEstado = Pedido.builder()
                .id(1L)
                .cliente(cliente)
                .listaProductos(listaProductos)
                .fecha(pedido.getFecha())
                .estado("COMPLETADO")
                .build();
        assertNotEquals(pedido, pedidoDiferenteEstado);
        
        // Test equals con diferente cliente
        Cliente otroCliente = Cliente.builder()
                .id(2L)
                .nombre("Maria")
                .email("maria@test.com")
                .build();
        Pedido pedidoDiferenteCliente = Pedido.builder()
                .id(1L)
                .cliente(otroCliente)
                .listaProductos(listaProductos)
                .fecha(pedido.getFecha())
                .estado("PENDIENTE")
                .build();
        assertNotEquals(pedido, pedidoDiferenteCliente);
        
        // Test equals con diferente lista de productos
        List<Producto> otraLista = new ArrayList<>();
        Producto otroProducto = Producto.builder()
                .id(2L)
                .nombre("Otro Producto")
                .precio(new BigDecimal("200.0"))
                .build();
        otraLista.add(otroProducto);
        
        Pedido pedidoDiferenteProductos = Pedido.builder()
                .id(1L)
                .cliente(cliente)
                .listaProductos(otraLista)
                .fecha(pedido.getFecha())
                .estado("PENDIENTE")
                .build();
        assertNotEquals(pedido, pedidoDiferenteProductos);
        
        // Test con campos null
        Pedido pedidoConNulls = new Pedido();
        pedidoConNulls.setId(1L);
        Pedido otroPedidoConNulls = new Pedido();
        otroPedidoConNulls.setId(1L);
        assertEquals(pedidoConNulls, otroPedidoConNulls);
    }

    @Test
    void testPedidoHashCodeExhaustive() {
        // Test hashCode consistencia
        int hashCode1 = pedido.hashCode();
        int hashCode2 = pedido.hashCode();
        assertEquals(hashCode1, hashCode2);
        
        // Test hashCode con objetos iguales
        Pedido pedidoIgual = Pedido.builder()
                .id(1L)
                .cliente(cliente)
                .listaProductos(listaProductos)
                .fecha(pedido.getFecha())
                .estado("PENDIENTE")
                .build();
        assertEquals(pedido.hashCode(), pedidoIgual.hashCode());
        
        // Test hashCode con campos null
        Pedido pedidoNulo = new Pedido();
        assertNotNull(pedidoNulo.hashCode()); // Debe poder calcular hashCode aunque tenga nulls
    }

    @Test
    void testCanEqual() {
        // Test canEqual con el mismo tipo
        assertTrue(pedido.canEqual(new Pedido()));
        
        // Test canEqual con tipo diferente  
        assertFalse(pedido.canEqual("string"));
        assertFalse(pedido.canEqual(null));
    }

    @Test
    void testPedidoToString() {
        assertNotNull(pedido.toString());
        assertTrue(pedido.toString().contains("Pedido"));
        assertTrue(pedido.toString().contains("id=1"));
        assertTrue(pedido.toString().contains("estado=PENDIENTE"));
    }

    @Test
    void testPedidoSettersAndGetters() {
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setId(2L);
        nuevoPedido.setCliente(cliente);
        nuevoPedido.setListaProductos(listaProductos);
        nuevoPedido.setEstado("COMPLETADO");
        
        LocalDateTime fecha = LocalDateTime.now();
        nuevoPedido.setFecha(fecha);

        assertEquals(2L, nuevoPedido.getId());
        assertEquals(cliente, nuevoPedido.getCliente());
        assertEquals(listaProductos, nuevoPedido.getListaProductos());
        assertEquals("COMPLETADO", nuevoPedido.getEstado());
        assertEquals(fecha, nuevoPedido.getFecha());
    }

    @Test
    void testPedidoWithNullValues() {
        Pedido pedidoVacio = new Pedido();
        
        assertNull(pedidoVacio.getId());
        assertNull(pedidoVacio.getCliente());
        assertNull(pedidoVacio.getListaProductos());
        assertNull(pedidoVacio.getEstado());
        assertNull(pedidoVacio.getFecha());
    }

    @Test
    void testPedidoAllArgsConstructor() {
        LocalDateTime fecha = LocalDateTime.now();
        Pedido pedidoCompleto = new Pedido(3L, fecha, "NUEVO", cliente, listaProductos);
        
        assertEquals(3L, pedidoCompleto.getId());
        assertEquals(fecha, pedidoCompleto.getFecha());
        assertEquals("NUEVO", pedidoCompleto.getEstado());
        assertEquals(cliente, pedidoCompleto.getCliente());
        assertEquals(listaProductos, pedidoCompleto.getListaProductos());
    }

    @Test
    void testPedidoWithEmptyProductList() {
        List<Producto> listaVacia = new ArrayList<>();
        pedido.setListaProductos(listaVacia);
        
        assertNotNull(pedido.getListaProductos());
        assertTrue(pedido.getListaProductos().isEmpty());
        assertEquals(0, pedido.getListaProductos().size());
    }

    @Test
    void testPedidoWithMultipleProducts() {
        Producto producto2 = Producto.builder()
                .id(2L)
                .nombre("Producto Test 2")
                .precio(new BigDecimal("200.0"))
                .build();

        listaProductos.add(producto2);
        pedido.setListaProductos(listaProductos);
        
        assertEquals(2, pedido.getListaProductos().size());
        assertTrue(pedido.getListaProductos().contains(producto));
        assertTrue(pedido.getListaProductos().contains(producto2));
    }
    
    @Test
    void testPedidoBuilderToString() {
        Pedido.PedidoBuilder builder = Pedido.builder()
                .id(1L)
                .cliente(cliente)
                .listaProductos(listaProductos)
                .fecha(LocalDateTime.now())
                .estado("PENDIENTE");
        
        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("PedidoBuilder"));
    }

    @Test
    void testEqualsEdgeCasesExhaustive() {
        // Test casos edge de equals para maximizar cobertura de branches
        
        // Caso 1: ID null vs no null
        Pedido p1 = new Pedido();
        p1.setId(null);
        p1.setEstado("PENDIENTE");
        
        Pedido p2 = new Pedido();
        p2.setId(1L);
        p2.setEstado("PENDIENTE");
        
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1); // Test simétrico
        
        // Caso 2: Fecha null vs no null
        LocalDateTime fecha = LocalDateTime.now();
        p1 = new Pedido();
        p1.setId(1L);
        p1.setFecha(null);
        
        p2 = new Pedido();
        p2.setId(1L);
        p2.setFecha(fecha);
        
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1);
        
        // Caso 3: Estado null vs no null
        p1 = new Pedido();
        p1.setId(1L);
        p1.setEstado(null);
        
        p2 = new Pedido();
        p2.setId(1L);
        p2.setEstado("PENDIENTE");
        
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1);
        
        // Caso 4: Cliente null vs no null
        p1 = new Pedido();
        p1.setId(1L);
        p1.setCliente(null);
        
        p2 = new Pedido();
        p2.setId(1L);
        p2.setCliente(cliente);
        
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1);
        
        // Caso 5: Lista productos null vs no null
        p1 = new Pedido();
        p1.setId(1L);
        p1.setListaProductos(null);
        
        p2 = new Pedido();
        p2.setId(1L);
        p2.setListaProductos(listaProductos);
        
        assertNotEquals(p1, p2);
        assertNotEquals(p2, p1);
        
        // Caso 6: Todos los campos null
        p1 = new Pedido();
        p2 = new Pedido();
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        
        // Caso 7: canEqual con null
        assertFalse(p1.canEqual(null));
        
        // Caso 8: Test hashCode con diferentes combinaciones null
        Pedido pNulo = new Pedido();
        assertNotEquals(0, pNulo.hashCode()); // Debe generar hash aunque sea null
        
        pNulo.setId(1L);
        int hash1 = pNulo.hashCode();
        pNulo.setEstado("TEST");
        int hash2 = pNulo.hashCode();
        assertNotEquals(hash1, hash2);
    }
}
