package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByClienteId(Long clienteId);

    List<Reserva> findByProductoId(Long productoId);

    List<Reserva> findByEstado(String estado);
}
