package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Reserva;
import com.letrasypapeles.backend.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

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
}
