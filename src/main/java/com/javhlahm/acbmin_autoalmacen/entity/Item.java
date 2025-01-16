package com.javhlahm.acbmin_autoalmacen.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

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

    @Column(nullable = false, unique = true)
    private String serie;

    private String descripcion;

    private int cantidad;

    private String modeloAuto;

    private String ultMovimiento;

    @Column(name = "fecha_creacion", updatable = false, nullable = false)
    private LocalDate fechaCreacion;

    private String notas;

    @Column(name = "aging", insertable = false, updatable = false)

    private Integer aging;

    private String localidad;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDate.now();
    }

    @Override
    public Item clone() {
        try {
            return (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
