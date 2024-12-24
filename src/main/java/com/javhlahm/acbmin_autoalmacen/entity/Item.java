package com.javhlahm.acbmin_autoalmacen.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "acbmin_almauto_inv")
public class Item implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_prod;

    private String nombre;

    private String categoria;

    private String marca;

    private String modelo;

    @Column(unique = true)
    private String serie;

    private String descripcion;

    private int cantidad;

    private String modeloAuto;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime ultMovimiento;

    private String notas;

    @Override
    public Item clone() {
        try {
            return (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
