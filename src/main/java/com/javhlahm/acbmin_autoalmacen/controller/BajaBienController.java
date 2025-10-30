package com.javhlahm.acbmin_autoalmacen.controller;

import com.javhlahm.acbmin_autoalmacen.entity.BajaBien;
import com.javhlahm.acbmin_autoalmacen.service.BajaBienServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bajas") // Ruta base para las bajasd
@CrossOrigin // Permite peticiones desde el frontend Flutter
public class BajaBienController {

    @Autowired
    private BajaBienServicio bajaBienServicio;

    // Endpoint para crear una nueva baja
    @PostMapping
    public ResponseEntity<BajaBien> crearBaja(@RequestBody BajaBien bajaBien) {
        BajaBien nuevaBaja = bajaBienServicio.crearBaja(bajaBien);
        // Devolvemos 201 Created junto con la baja creada (que ahora tendrá folio)
        return new ResponseEntity<>(nuevaBaja, HttpStatus.CREATED);
    }

    // Endpoint para obtener todas las bajas
    @GetMapping
    public ResponseEntity<List<BajaBien>> obtenerTodasLasBajas() {
        List<BajaBien> bajas = bajaBienServicio.obtenerTodasLasBajas();
        return ResponseEntity.ok(bajas); // Devuelve 200 OK con la lista
    }

    // Endpoint para obtener una baja por folio
    @GetMapping("/{folio}")
    public ResponseEntity<BajaBien> obtenerBajaPorFolio(@PathVariable int folio) {
        Optional<BajaBien> bajaOpt = bajaBienServicio.obtenerBajaPorFolio(folio);
        // Si se encuentra, devuelve 200 OK con la baja, si no, 404 Not Found
        return bajaOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para actualizar una baja por folio
    @PutMapping("/{folio}")
    public ResponseEntity<BajaBien> actualizarBaja(@PathVariable int folio, @RequestBody BajaBien bajaActualizada) {
        Optional<BajaBien> bajaOpt = bajaBienServicio.actualizarBaja(folio, bajaActualizada);
        // Si se actualiza, devuelve 200 OK con la baja actualizada, si no, 404 Not
        // Found
        return bajaOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para eliminar una baja por folio
    @DeleteMapping("/{folio}")
    public ResponseEntity<Void> eliminarBaja(@PathVariable int folio) {
        boolean eliminado = bajaBienServicio.eliminarBaja(folio);
        // Si se elimina, devuelve 204 No Content, si no, 404 Not Found
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Endpoint para generar el reporte PDF de una baja
    @GetMapping("/{folio}/pdf")
    public ResponseEntity<byte[]> generarReporteBaja(@PathVariable int folio) {
        try {
            byte[] pdfBytes = bajaBienServicio.exportReportBaja(folio);

            HttpHeaders headers = new HttpHeaders();
            // Establece el tipo de contenido como PDF
            headers.setContentType(MediaType.APPLICATION_PDF);
            // Sugiere al navegador descargar el archivo con un nombre específico
            String filename = "Baja_Bien_" + folio + ".pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            // Devuelve los bytes del PDF con las cabeceras adecuadas
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            // Otros errores inesperados
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error inesperado: " + e.getMessage()).getBytes());
        }
    }
}