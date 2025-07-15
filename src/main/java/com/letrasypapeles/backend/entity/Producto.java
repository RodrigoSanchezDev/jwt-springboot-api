package com.letrasypapeles.backend.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Entidad que representa un producto en el inventario")
@Entity
@Table(name = "productos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Schema(description = "ID único del producto", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del producto", example = "Cuaderno Universitario")
    private String nombre;

    @Schema(description = "Descripción detallada del producto", example = "Cuaderno de 100 hojas rayadas")
    private String descripcion;

    @Schema(description = "Precio del producto", example = "15.50")
    private BigDecimal precio;

    @Schema(description = "Stock disponible del producto", example = "100")
    private Integer stock;

    @Schema(description = "Categoría a la que pertenece el producto")
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Schema(description = "Proveedor del producto")
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    // Método para actualizar el stock tras una reserva
    public void actualizarStockTrasReserva(int cantidadReservada) {
        if (cantidadReservada <= this.stock) {
            this.stock -= cantidadReservada;
        } else {
            throw new IllegalArgumentException("Cantidad reservada excede el stock disponible.");
        }
    }
}
