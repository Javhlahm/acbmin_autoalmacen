package com.javhlahm.acbmin_autoalmacen.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bajas_bienes") // Nombre sugerido para la tabla en la base de datos
@Data // Lombok para generar getters, setters, toString, etc. automáticamente
public class BajaBien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int folio; // Folio autoincremental como ID

    @Column(name = "fecha_autorizado")
    private LocalDateTime fechaAutorizado; // Usamos LocalDateTime si necesitas hora

    @Column(name = "area_baja", nullable = false)
    private String areaBaja;

    @Column(name = "persona_baja", nullable = false)
    private String personaBaja;

    @Column(name = "rfc_baja", nullable = false)
    private String rfcBaja;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "numero_inventario", nullable = false)
    private String numeroInventario;

    @Column(name = "fecha_entrega_activos")
    private LocalDate fechaEntregaActivos; // Usamos LocalDate si solo necesitas la fecha

    @Column
    private String estatus; // Ej: Pendiente, Aprobado, Rechazado

    @Column(name = "capturado_por")
    private String capturadoPor;

    @Column(length = 1000) // Más espacio para observaciones
    private String observaciones;

    // Constructores, Getters y Setters generados por Lombok (@Data)
    // Si no usas Lombok, debes añadirlos manualmente.
}