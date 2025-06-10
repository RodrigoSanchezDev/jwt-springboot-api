package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Cliente;
import com.letrasypapeles.backend.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor   
public class UsuarioService implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1) Buscar el cliente por e-mail
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado con e-mail: " + email));

        // 2) Convertir los roles de la entidad en GrantedAuthority SIN “ROLE_”
        Collection<GrantedAuthority> authorities = cliente.getRoles()
                .stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))   // ← aquí el cambio
                .collect(Collectors.toList());

        logger.info("Loaded user: {} with roles from DB: {} and authorities: {}", 
                    email, 
                    cliente.getRoles().stream().map(r->r.getNombre()).toList(), 
                    authorities);

        // 3) Devolver un UserDetails con e-mail, hash de contraseña y authorities
        return new User(
                cliente.getEmail(),
                cliente.getContraseña(),   
                authorities
        );
    }
}
