package com.javhlahm.acbmin_autoalmacen.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "resguardos_internos") // Nombre sugerido para la tabla
@Data // Lombok para getters/setters/etc.
public class Resguardo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resguardo_seq")
    @SequenceGenerator(name = "resguardo_seq", sequenceName = "resguardos_internos_folio_seq", allocationSize = 1, initialValue = 100) // Empieza en 100
    private Integer folio; // Autogenerado por secuencia, empieza en 100

    @Column(name = "tipo_resguardo", nullable = false, length = 50)
    private String tipoResguardo; // "Traspaso de resguardo" o "Nueva adquisición"

    @Column(name = "numero_inventario", nullable = false, length = 100)
    private String numeroInventario;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(name = "area_entrega", nullable = false, length = 150)
    private String areaEntrega; // "N/A" si es nueva adquisición

    @Column(name = "nombre_entrega", nullable = false, length = 200)
    private String nombreEntrega; // "N/A" si es nueva adquisición

    @Column(name = "rfc_entrega", nullable = false, length = 20)
    private String rfcEntrega; // "N/A" si es nueva adquisición

    @Column(name = "area_recibe", nullable = false, length = 150)
    private String areaRecibe;

    @Column(name = "nombre_recibe", nullable = false, length = 200)
    private String nombreRecibe;

    @Column(name = "rfc_recibe", nullable = false, length = 20)
    private String rfcRecibe;

    @Column(length = 1000) // Más espacio para observaciones
    private String observaciones;

    @Column(name = "capturado_por", nullable = false, length = 200)
    private String capturadoPor;

    @Column(name = "fecha_autorizado") // Puede ser nulo
    private LocalDateTime fechaAutorizado;

    @Column(nullable = false, length = 20)
    private String estatus; // "Pendiente", "Aprobado", "Rechazado"

    // Lombok @Data genera constructores, getters, setters, toString, equals, hashCode
}
