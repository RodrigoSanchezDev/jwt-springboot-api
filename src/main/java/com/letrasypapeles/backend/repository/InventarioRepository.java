package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    List<Inventario> findByProductoId(Long productoId);

    List<Inventario> findBySucursalId(Long sucursalId);

    List<Inventario> findByCantidadLessThan(Integer umbral);
}
