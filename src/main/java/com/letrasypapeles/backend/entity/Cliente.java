package com.letrasypapeles.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "clientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String email;

    @Column(name = "contrasena")
    private String contraseña; 

    private Integer puntosFidelidad;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "clientes_roles",
        joinColumns = @JoinColumn(name = "cliente_id"),
        inverseJoinColumns = @JoinColumn(name = "role_nombre")
    )
    private Set<Role> roles;
}
