package com.letrasypapeles.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una notificación del sistema")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la notificación", example = "1")
    private Long id;

    @Schema(description = "Mensaje de la notificación", example = "Su pedido ha sido procesado")
    private String mensaje;

    @Schema(description = "Fecha y hora de la notificación", example = "2024-01-15T10:30:00")
    private LocalDateTime fecha;

    @Column(name = "leida", nullable = false)
    @Builder.Default
    @Schema(description = "Indica si la notificación ha sido leída", example = "false")
    private Boolean leida = false;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @Schema(description = "Cliente asociado a la notificación")
    private Cliente cliente;
}
