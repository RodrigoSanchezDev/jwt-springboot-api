package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.entity.Role;
import com.letrasypapeles.backend.repository.ClienteRepository;
import com.letrasypapeles.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Cliente> obtenerTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> obtenerPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Cliente registrarCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }

        String rawPassword = cliente.getContraseña();
        String hashed = passwordEncoder.encode(rawPassword);
        cliente.setContraseña(hashed);

        cliente.setPuntosFidelidad(0);

        Role rolCliente = roleRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("El rol 'CLIENTE' no existe en la BD."));
        cliente.setRoles(Collections.singleton(rolCliente));

        return clienteRepository.save(cliente);
    }

    public Cliente registrarClienteConRol(Cliente cliente, String rolNombre) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }
        String rawPassword = cliente.getContraseña();
        String hashed = passwordEncoder.encode(rawPassword);
        cliente.setContraseña(hashed);
        cliente.setPuntosFidelidad(0);

        String rolFinal = (rolNombre == null || rolNombre.isBlank()) ? "CLIENTE" : rolNombre.trim().toUpperCase();
        Role rol = roleRepository.findByNombre(rolFinal)
                .orElseThrow(() -> new RuntimeException("El rol '" + rolFinal + "' no existe en la BD."));
        logger.info("Asignando rol '{}' al cliente {}", rol.getNombre(), cliente.getEmail());
        cliente.setRoles(Collections.singleton(rol));
        logger.info("Roles antes de guardar: {}", cliente.getRoles());

        clienteRepository.save(cliente);
        Cliente recargado = clienteRepository.findByEmail(cliente.getEmail()).orElseThrow();
        logger.info("Roles después de guardar y recargar: {}", recargado.getRoles());
        return recargado;
    }

    public Cliente actualizarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }
}
