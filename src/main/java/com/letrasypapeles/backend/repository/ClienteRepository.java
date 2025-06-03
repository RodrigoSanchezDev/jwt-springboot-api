package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Cliente;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<Cliente> findByEmail(String email);

    boolean existsByEmail(String email);
}
