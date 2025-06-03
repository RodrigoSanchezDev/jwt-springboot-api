package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Métodos personalizados si los necesitas
}
