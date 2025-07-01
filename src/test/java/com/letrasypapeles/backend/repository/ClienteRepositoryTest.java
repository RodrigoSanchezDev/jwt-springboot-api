package com.letrasypapeles.backend.repository;

import com.letrasypapeles.backend.entity.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @Transactional
    @Rollback
    void testFindByEmail() {
        Cliente cliente = Cliente.builder()
                .nombre("Juan")
                .apellido("Perez")
                .email("juan.perez@example.com")
                .build();

        clienteRepository.save(cliente);

        Optional<Cliente> result = clienteRepository.findByEmail("juan.perez@example.com");

        assertTrue(result.isPresent());
        assertEquals("juan.perez@example.com", result.get().getEmail());
    }
}
