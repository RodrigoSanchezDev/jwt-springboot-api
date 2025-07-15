package com.letrasypapeles.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Schema(description = "Entidad que representa un cliente del sistema")
@Entity
@Table(name = "clientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Schema(description = "ID único del cliente", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del cliente", example = "Juan")
    private String nombre;
    
    @Schema(description = "Apellido del cliente", example = "Pérez")
    private String apellido;
    
    @Schema(description = "Email del cliente", example = "juan.perez@email.com")
    private String email;

    @Schema(description = "Contraseña del cliente", hidden = true)
    @JsonIgnore
    @Column(name = "contrasena")
    private String contraseña; 

    @Schema(description = "Puntos de fidelidad acumulados", example = "150")
    private Integer puntosFidelidad;

    @Schema(description = "Roles asignados al cliente")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "clientes_roles",
        joinColumns = @JoinColumn(name = "cliente_id"),
        inverseJoinColumns = @JoinColumn(name = "role_nombre")
    )
    private Set<Role> roles;

    // Métodos para gestionar roles
    public void agregarRol(Role role) {
        this.roles.add(role);
    }

    public void eliminarRol(Role role) {
        this.roles.remove(role);
    }

    public Set<Role> obtenerRoles() {
        return this.roles;
    }
}
