package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.*;
import com.letrasypapeles.backend.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ReservaServiceTest {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private InventarioService inventarioService;

    @Mock
    private ProveedorService proveedorService;

    @Test
    void testGuardarReserva() {
        Reserva reserva = Reserva.builder()
                .id(1L)
                .estado("Confirmada")
                .cantidad(5)
                .build();

        Mockito.when(reservaRepository.save(reserva)).thenReturn(reserva);

        Reserva result = reservaService.guardar(reserva);

        assertNotNull(result);
        assertEquals("Confirmada", result.getEstado());
    }

    @Test
    void testObtenerPorId() {
        Reserva reserva = Reserva.builder()
                .id(1L)
                .estado("Confirmada")
                .cantidad(5)
                .build();

        Mockito.when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Optional<Reserva> result = reservaService.obtenerPorId(1L);

        assertTrue(result.isPresent());
        assertEquals("Confirmada", result.get().getEstado());
    }

    @Test
    void testObtenerTodas() {
        List<Reserva> reservas = Arrays.asList(
                Reserva.builder().id(1L).estado("Confirmada").cantidad(5).build(),
                Reserva.builder().id(2L).estado("Pendiente").cantidad(3).build()
        );

        Mockito.when(reservaRepository.findAll()).thenReturn(reservas);

        List<Reserva> result = reservaService.obtenerTodas();

        assertEquals(2, result.size());
        assertEquals("Confirmada", result.get(0).getEstado());
        assertEquals("Pendiente", result.get(1).getEstado());
    }

    @Test
    void testEliminar() {
        Mockito.doNothing().when(reservaRepository).deleteById(1L);

        assertDoesNotThrow(() -> reservaService.eliminar(1L));
        Mockito.verify(reservaRepository).deleteById(1L);
    }

    @Test
    void testObtenerPorClienteId() {
        List<Reserva> reservas = Arrays.asList(
                Reserva.builder().id(1L).estado("Confirmada").cantidad(5).build()
        );

        Mockito.when(reservaRepository.findByClienteId(1L)).thenReturn(reservas);

        List<Reserva> result = reservaService.obtenerPorClienteId(1L);

        assertEquals(1, result.size());
        assertEquals("Confirmada", result.get(0).getEstado());
    }

    @Test
    void testObtenerPorProductoId() {
        List<Reserva> reservas = Arrays.asList(
                Reserva.builder().id(1L).estado("Confirmada").cantidad(5).build()
        );

        Mockito.when(reservaRepository.findByProductoId(1L)).thenReturn(reservas);

        List<Reserva> result = reservaService.obtenerPorProductoId(1L);

        assertEquals(1, result.size());
        assertEquals("Confirmada", result.get(0).getEstado());
    }

    @Test
    void testObtenerPorEstado() {
        List<Reserva> reservas = Arrays.asList(
                Reserva.builder().id(1L).estado("Confirmada").cantidad(5).build()
        );

        Mockito.when(reservaRepository.findByEstado("Confirmada")).thenReturn(reservas);

        List<Reserva> result = reservaService.obtenerPorEstado("Confirmada");

        assertEquals(1, result.size());
        assertEquals("Confirmada", result.get(0).getEstado());
    }

    @Test
    void testGuardarConValidacion_ConStock() {
        Producto producto = Producto.builder().id(1L).nombre("Test Product").build();
        Reserva reserva = Reserva.builder()
                .id(1L)
                .estado("Pendiente")
                .cantidad(5)
                .producto(producto)
                .build();

        List<Inventario> inventarios = Arrays.asList(
                Inventario.builder().cantidad(10).build(),
                Inventario.builder().cantidad(5).build()
        );

        Mockito.when(inventarioService.obtenerPorProductoId(1L)).thenReturn(inventarios);
        Mockito.when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Reserva result = reservaService.guardarConValidacion(reserva);

        assertEquals("Confirmada", result.getEstado());
        Mockito.verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void testGuardarConValidacion_SinStock() {
        Producto producto = Producto.builder().id(1L).nombre("Test Product").build();
        Reserva reserva = Reserva.builder()
                .id(1L)
                .estado("Pendiente")
                .cantidad(5)
                .producto(producto)
                .build();

        List<Inventario> inventarios = Arrays.asList();

        Mockito.when(inventarioService.obtenerPorProductoId(1L)).thenReturn(inventarios);
        Mockito.when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Reserva result = reservaService.guardarConValidacion(reserva);

        assertEquals("Rechazada", result.getEstado());
        Mockito.verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void testGuardarConProveedorValidado_ProveedorCompleto() {
        Proveedor proveedor = Proveedor.builder()
                .id(1L)
                .nombre("Proveedor Test")
                .contacto("test@test.com")
                .build();

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Test Product")
                .proveedor(proveedor)
                .build();

        Reserva reserva = Reserva.builder()
                .id(1L)
                .estado("Pendiente")
                .cantidad(5)
                .producto(producto)
                .build();

        List<Inventario> inventarios = Arrays.asList(
                Inventario.builder().cantidad(10).build()
        );

        Mockito.when(inventarioService.obtenerPorProductoId(1L)).thenReturn(inventarios);
        Mockito.when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Reserva result = reservaService.guardarConProveedorValidado(reserva);

        assertEquals("Confirmada", result.getEstado());
    }

    @Test
    void testGuardarConProveedorValidado_ProveedorIncompleto() {
        Proveedor proveedor = Proveedor.builder()
                .id(1L)
                .nombre("Proveedor Test")
                .contacto(null)
                .build();

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Test Product")
                .proveedor(proveedor)
                .build();

        Reserva reserva = Reserva.builder()
                .id(1L)
                .estado("Pendiente")
                .cantidad(5)
                .producto(producto)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.guardarConProveedorValidado(reserva);
        });

        assertEquals("El proveedor asignado no tiene información completa.", exception.getMessage());
    }

    @Test
    void testGuardarConProveedorValidado_ProveedorNulo() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Test Product")
                .proveedor(null)
                .build();

        Reserva reserva = Reserva.builder()
                .id(1L)
                .estado("Pendiente")
                .cantidad(5)
                .producto(producto)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.guardarConProveedorValidado(reserva);
        });

        assertEquals("El proveedor asignado no tiene información completa.", exception.getMessage());
    }
    
    @Test
    void testGuardarConValidacion_StockInsuficiente() {
        Producto producto = Producto.builder().id(1L).nombre("Test Product").build();
        Reserva reserva = Reserva.builder()
                .id(1L)
                .estado("Pendiente")
                .cantidad(20)  // Cantidad mayor al stock disponible
                .producto(producto)
                .build();

        List<Inventario> inventarios = Arrays.asList(
                Inventario.builder().cantidad(5).build(),
                Inventario.builder().cantidad(3).build()
        ); // Total stock = 8, pero se necesitan 20

        Mockito.when(inventarioService.obtenerPorProductoId(1L)).thenReturn(inventarios);
        Mockito.when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Reserva result = reservaService.guardarConValidacion(reserva);

        assertEquals("Rechazada", result.getEstado());
        Mockito.verify(reservaRepository).save(any(Reserva.class));
    }
    
    @Test
    void testGuardarConProveedorValidado_ProveedorSinNombre() {
        Proveedor proveedor = Proveedor.builder()
                .id(1L)
                .nombre(null)
                .contacto("test@test.com")
                .build();

        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Test Product")
                .proveedor(proveedor)
                .build();

        Reserva reserva = Reserva.builder()
                .id(1L)
                .estado("Pendiente")
                .cantidad(5)
                .producto(producto)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.guardarConProveedorValidado(reserva);
        });

        assertEquals("El proveedor asignado no tiene información completa.", exception.getMessage());
    }
}