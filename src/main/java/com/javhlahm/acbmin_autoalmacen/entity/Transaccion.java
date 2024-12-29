package com.javhlahm.acbmin_autoalmacen.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "acbmin_almauto_transact")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_trans;

    private String tipo;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fecha;

    private long id_prod;

    private int cantidad;

    private String placa;

    private String nombre;

    private String categoria;

    private String marca;

    private String modelo;

    private String serie;

    private String descripcion;

    private String modeloAuto;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime ultMovimiento;

    private String notas;

}
