package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    // Métodos personalizados si los necesitas
}
