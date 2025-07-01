package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.entity.Producto;
import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProveedorService proveedorService;

    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> obtenerPorId(Long id) {
        return reservaRepository.findById(id);
    }

    public Reserva guardar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public void eliminar(Long id) {
        reservaRepository.deleteById(id);
    }

    public List<Reserva> obtenerPorClienteId(Long clienteId) {
        return reservaRepository.findByClienteId(clienteId);
    }

    public List<Reserva> obtenerPorProductoId(Long productoId) {
        return reservaRepository.findByProductoId(productoId);
    }

    public List<Reserva> obtenerPorEstado(String estado) {
        return reservaRepository.findByEstado(estado);
    }

    public Reserva guardarConValidacion(Reserva reserva) {
        List<Inventario> inventarios = inventarioService.obtenerPorProductoId(reserva.getProducto().getId());
        int totalStock = inventarios.stream().mapToInt(Inventario::getCantidad).sum();

        if (totalStock >= reserva.getCantidad()) {
            reserva.setEstado("Confirmada");
            return reservaRepository.save(reserva);
        } else {
            reserva.setEstado("Rechazada");
            return reservaRepository.save(reserva);
        }
    }

    public Reserva guardarConProveedorValidado(Reserva reserva) {
        Proveedor proveedor = reserva.getProducto().getProveedor();
        if (proveedor == null || proveedor.getNombre() == null || proveedor.getContacto() == null) {
            throw new RuntimeException("El proveedor asignado no tiene información completa.");
        }

        return guardarConValidacion(reserva);
    }
}
