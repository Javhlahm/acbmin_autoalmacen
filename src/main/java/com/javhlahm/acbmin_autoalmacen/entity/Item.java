package com.javhlahm.acbmin_autoalmacen.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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
@Table(name = "acbmin_almauto")
public class Item implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;

    private String categoria;

    private String marca;

    private String modelo;

    @Column(unique = true)
    private String serie;

    private String descripcion;

    private int stockDisponible;

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
