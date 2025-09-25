package com.javhlahm.acbmin_autoalmacen.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "acbmin_usuarios")
public class UsuariosAcbmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nombre;

    @Column(unique = true)
    private String email;

    private String contrasena;

    private List<String> roles;

    private String status;

}
