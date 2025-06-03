package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    // Métodos personalizados si los necesitas
}
