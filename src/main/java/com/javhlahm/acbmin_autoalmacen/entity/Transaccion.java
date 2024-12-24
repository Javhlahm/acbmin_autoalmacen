package com.javhlahm.acbmin_autoalmacen.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "acbmin_almauto_transac")
public class Transaccion extends Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_trans;

    private String tipo;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fecha;

    private long id_prod;

    private int cantidad;

    private String placa;

}
