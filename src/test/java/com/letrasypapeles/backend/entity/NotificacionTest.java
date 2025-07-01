package com.letrasypapeles.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class NotificacionTest {

    private Notificacion notificacion;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .id(1L)
                .nombre("Juan")
                .email("juan@test.com")
                .build();

        notificacion = Notificacion.builder()
                .id(1L)
                .mensaje("Notificación de prueba")
                .fecha(LocalDateTime.now())
                .cliente(cliente)
                .build();
    }

    @Test
    void testNotificacionBuilder() {
        assertNotNull(notificacion);
        assertEquals(1L, notificacion.getId());
        assertEquals("Notificación de prueba", notificacion.getMensaje());
        assertEquals(cliente, notificacion.getCliente());
        assertNotNull(notificacion.getFecha());
    }

    @Test
    void testConstructorVacio() {
        Notificacion notificacionVacia = new Notificacion();
        
        assertNotNull(notificacionVacia);
        assertNull(notificacionVacia.getId());
        assertNull(notificacionVacia.getMensaje());
        assertNull(notificacionVacia.getFecha());
        assertNull(notificacionVacia.getCliente());
    }

    @Test
    void testSettersAndGetters() {
        Notificacion n = new Notificacion();
        LocalDateTime fecha = LocalDateTime.now();
        
        n.setId(2L);
        n.setMensaje("Nueva notificación");
        n.setFecha(fecha);
        n.setCliente(cliente);
        
        assertEquals(2L, n.getId());
        assertEquals("Nueva notificación", n.getMensaje());
        assertEquals(fecha, n.getFecha());
        assertEquals(cliente, n.getCliente());
    }

    @Test
    void testValidarMensajeNoVacio() {
        // Validar que el mensaje no esté vacío
        assertNotNull(notificacion.getMensaje());
        assertFalse(notificacion.getMensaje().isEmpty());
    }

    @Test
    void testValidarFechaReciente() {
        // Validar que la fecha sea reciente (menos de 1 minuto de diferencia)
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime fechaNotificacion = notificacion.getFecha();
        
        assertTrue(fechaNotificacion.isBefore(ahora.plusMinutes(1)));
        assertTrue(fechaNotificacion.isAfter(ahora.minusMinutes(1)));
    }

    @Test
    void testValidarClienteAsignado() {
        // Validar que tenga un cliente asignado
        assertNotNull(notificacion.getCliente());
        assertEquals(cliente.getId(), notificacion.getCliente().getId());
    }

    @Test
    void testEsNotificacionReciente() {
        // Método de lógica para determinar si es reciente (último día)
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime hace24Horas = ahora.minusDays(1);
        
        boolean esReciente = notificacion.getFecha().isAfter(hace24Horas);
        assertTrue(esReciente);
    }

    @Test
    void testToString() {
        String toString = notificacion.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Notificación de prueba"));
    }

    @Test
    void testEqualsAndHashCode() {
        Notificacion notificacion1 = Notificacion.builder()
                .id(1L)
                .mensaje("Test")
                .fecha(LocalDateTime.now())
                .cliente(cliente)
                .build();
                
        Notificacion notificacion2 = Notificacion.builder()
                .id(1L)
                .mensaje("Test")
                .fecha(notificacion1.getFecha())
                .cliente(cliente)
                .build();
                
        assertEquals(notificacion1, notificacion2);
        assertEquals(notificacion1.hashCode(), notificacion2.hashCode());
    }

    @Test
    void testEqualsWithNull() {
        assertFalse(notificacion.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertFalse(notificacion.equals("not a notificacion"));
    }

    @Test
    void testEqualsWithSameInstance() {
        assertTrue(notificacion.equals(notificacion));
    }

    @Test
    void testEqualsWithDifferentId() {
        Notificacion otra = Notificacion.builder()
                .id(2L)
                .mensaje(notificacion.getMensaje())
                .fecha(notificacion.getFecha())
                .cliente(notificacion.getCliente())
                .build();
        
        assertFalse(notificacion.equals(otra));
    }

    @Test
    void testEqualsConMensajeDiferente() {
        Notificacion otra = Notificacion.builder()
                .id(1L)
                .mensaje("Mensaje diferente")
                .fecha(notificacion.getFecha())
                .cliente(notificacion.getCliente())
                .build();
        
        assertNotEquals(notificacion, otra);
    }

    @Test
    void testEqualsConFechaDiferente() {
        Notificacion otra = Notificacion.builder()
                .id(1L)
                .mensaje(notificacion.getMensaje())
                .fecha(LocalDateTime.now().plusHours(1))
                .cliente(notificacion.getCliente())
                .build();
        
        assertNotEquals(notificacion, otra);
    }

    @Test
    void testEqualsConClienteDiferente() {
        Cliente otroCliente = Cliente.builder()
                .id(2L)
                .nombre("Pedro")
                .email("pedro@test.com")
                .build();
                
        Notificacion otra = Notificacion.builder()
                .id(1L)
                .mensaje(notificacion.getMensaje())
                .fecha(notificacion.getFecha())
                .cliente(otroCliente)
                .build();
        
        assertNotEquals(notificacion, otra);
    }

    @Test
    void testEqualsConIdNull() {
        Notificacion notif1 = Notificacion.builder()
                .mensaje("Test")
                .fecha(LocalDateTime.now())
                .cliente(cliente)
                .build();
                
        Notificacion notif2 = Notificacion.builder()
                .mensaje("Test")
                .fecha(notif1.getFecha())
                .cliente(cliente)
                .build();
        
        assertEquals(notif1, notif2);
        
        Notificacion notif3 = Notificacion.builder()
                .id(1L)
                .mensaje("Test")
                .fecha(notif1.getFecha())
                .cliente(cliente)
                .build();
        
        assertNotEquals(notif1, notif3);
    }

    @Test
    void testEqualsConMensajeNull() {
        Notificacion notif1 = Notificacion.builder()
                .id(1L)
                .fecha(LocalDateTime.now())
                .cliente(cliente)
                .build();
                
        Notificacion notif2 = Notificacion.builder()
                .id(1L)
                .fecha(notif1.getFecha())
                .cliente(cliente)
                .build();
        
        assertEquals(notif1, notif2);
        
        Notificacion notif3 = Notificacion.builder()
                .id(1L)
                .mensaje("Test")
                .fecha(notif1.getFecha())
                .cliente(cliente)
                .build();
        
        assertNotEquals(notif1, notif3);
    }

    @Test
    void testEqualsConFechaNull() {
        Notificacion notif1 = Notificacion.builder()
                .id(1L)
                .mensaje("Test")
                .cliente(cliente)
                .build();
                
        Notificacion notif2 = Notificacion.builder()
                .id(1L)
                .mensaje("Test")
                .cliente(cliente)
                .build();
        
        assertEquals(notif1, notif2);
        
        Notificacion notif3 = Notificacion.builder()
                .id(1L)
                .mensaje("Test")
                .fecha(LocalDateTime.now())
                .cliente(cliente)
                .build();
        
        assertNotEquals(notif1, notif3);
    }

    @Test
    void testEqualsConClienteNull() {
        Notificacion notif1 = Notificacion.builder()
                .id(1L)
                .mensaje("Test")
                .fecha(LocalDateTime.now())
                .build();
                
        Notificacion notif2 = Notificacion.builder()
                .id(1L)
                .mensaje("Test")
                .fecha(notif1.getFecha())
                .build();
        
        assertEquals(notif1, notif2);
        
        Notificacion notif3 = Notificacion.builder()
                .id(1L)
                .mensaje("Test")
                .fecha(notif1.getFecha())
                .cliente(cliente)
                .build();
        
        assertNotEquals(notif1, notif3);
    }

    @Test
    void testCanEqual() {
        assertTrue(notificacion.canEqual(new Notificacion()));
        assertFalse(notificacion.canEqual("Una string"));
        assertFalse(notificacion.canEqual(null));
    }

    @Test
    void testHashCodeConsistencia() {
        int hashCode1 = notificacion.hashCode();
        int hashCode2 = notificacion.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCodeConValoresNull() {
        Notificacion notifConNulls = new Notificacion();
        int hashCode = notifConNulls.hashCode();
        // Solo verificamos que no lance excepción
        assertNotNull(hashCode);
    }

    @Test
    void testBuilderMetodos() {
        Notificacion.NotificacionBuilder builder = Notificacion.builder();
        assertNotNull(builder);
        
        LocalDateTime fecha = LocalDateTime.now();
        // Test de todos los métodos del builder
        Notificacion notifCompleta = builder
                .id(10L)
                .mensaje("Builder Test")
                .fecha(fecha)
                .cliente(cliente)
                .build();
        
        assertEquals(10L, notifCompleta.getId());
        assertEquals("Builder Test", notifCompleta.getMensaje());
        assertEquals(fecha, notifCompleta.getFecha());
        assertEquals(cliente, notifCompleta.getCliente());
    }

    @Test
    void testBuilderToString() {
        Notificacion.NotificacionBuilder builder = Notificacion.builder()
                .id(5L)
                .mensaje("Test Builder");
        
        String builderString = builder.toString();
        assertNotNull(builderString);
        assertTrue(builderString.contains("NotificacionBuilder"));
    }

    @Test
    void testToStringWithNullFields() {
        Notificacion notificacionVacia = new Notificacion();
        String toString = notificacionVacia.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("Notificacion"));
        assertTrue(toString.contains("null"));
    }
}
