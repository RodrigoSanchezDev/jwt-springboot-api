package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Métodos personalizados si es necesario
}
