package com.letrasypapeles.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    private Integer umbral;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    private int stockActual;
    private int umbralMinimo;

    // Constructor explícito para inicializar valores
    public Inventario(int stockActual, int umbralMinimo) {
        this.stockActual = stockActual;
        this.umbralMinimo = umbralMinimo;
    }

    // Métodos para establecer y obtener el stock actual
    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockActual() {
        return this.stockActual;
    }

    // Métodos para establecer y obtener el umbral mínimo
    public void setUmbralMinimo(int umbralMinimo) {
        this.umbralMinimo = umbralMinimo;
    }

    public int getUmbralMinimo() {
        return this.umbralMinimo;
    }

    // Método para verificar si se necesita reabastecimiento con validación adicional
    public boolean necesitaReabastecimiento() {
        System.out.println("Método necesitaReabastecimiento ejecutado");
        System.out.println("Stock actual antes de comparación: " + this.stockActual);
        System.out.println("Umbral mínimo antes de comparación: " + this.umbralMinimo);
        System.out.println("Comparación: " + (this.stockActual < this.umbralMinimo));
        if (this.stockActual < this.umbralMinimo) {
            System.out.println("Resultado: true");
            return true;
        } else {
            System.out.println("Resultado: false");
            return false;
        }
    }
}
