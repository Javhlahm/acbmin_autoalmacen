package com.javhlahm.acbmin_autoalmacen.controller;

import com.javhlahm.acbmin_autoalmacen.entity.Resguardo;
import com.javhlahm.acbmin_autoalmacen.service.ResguardoServicio;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping("/api/resguardos") // Ruta base actualizada
@CrossOrigin // Permitir peticiones desde el frontend Flutter
public class ResguardoController {

    @Autowired
    private ResguardoServicio resguardoServicio;

    // Obtener todos los resguardos
    @GetMapping
    public ResponseEntity<List<Resguardo>> getAllResguardos() {
        try {
            List<Resguardo> resguardos = resguardoServicio.getAllResguardos();
            return ResponseEntity.ok(resguardos);
        } catch (Exception e) {
             System.err.println("Error al obtener resguardos: " + e.getMessage());
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al obtener resguardos", e);
        }
    }

    // Obtener un resguardo por folio
     @GetMapping("/{folio}")
     public ResponseEntity<Resguardo> getResguardoByFolio(@PathVariable Integer folio) {
         return resguardoServicio.getResguardoByFolio(folio)
                 .map(ResponseEntity::ok)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resguardo no encontrado con folio: " + folio));
     }


    // Crear un nuevo resguardo
    @PostMapping
    public ResponseEntity<Resguardo> createResguardo(@RequestBody Resguardo resguardo) {
        try {
            // Se asume que el frontend envía todos los datos iniciales necesarios y validados
            Resguardo nuevoResguardo = resguardoServicio.createResguardo(resguardo);
            return new ResponseEntity<>(nuevoResguardo, HttpStatus.CREATED);
        } catch (Exception e) { // Captura errores generales (ej. BD)
            System.err.println("Error al crear resguardo: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al crear resguardo", e);
        }
    }

    // Actualizar un resguardo existente (incluye lógica de estatus manejada en el servicio)
    @PutMapping("/{folio}")
    public ResponseEntity<Resguardo> updateResguardo(@PathVariable Integer folio, @RequestBody Resguardo resguardoDetails) {
        try {
            // Se asume que el frontend envía todos los datos necesarios y validados
            Resguardo updatedResguardo = resguardoServicio.updateResguardo(folio, resguardoDetails);
            return ResponseEntity.ok(updatedResguardo);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) { // Captura errores generales
            System.err.println("Error al actualizar resguardo " + folio + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al actualizar resguardo", e);
        }
    }

    // Eliminar un resguardo
    @DeleteMapping("/{folio}")
    public ResponseEntity<Void> deleteResguardo(@PathVariable Integer folio) {
        try {
            resguardoServicio.deleteResguardo(folio);
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Error al eliminar resguardo " + folio + ": " + e.getMessage());
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno al eliminar resguardo", e);
        }
    }

    // --- ENDPOINT PARA GENERAR PDF ---
    @GetMapping("/{folio}/pdf")
    // La autorización la maneja la regla general en SecurityConfig para /api/resguardos/**
    public ResponseEntity<byte[]> getResguardoPdf(@PathVariable Integer folio) {
        try {
            byte[] pdfBytes = resguardoServicio.generarPdfResguardo(folio);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "resguardo_" + String.format("%04d", folio) + ".pdf";

             // Forzar descarga:
             headers.setContentDispositionFormData("attachment", filename);

            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);


        } catch (Exception e) {
             System.err.println("Error inesperado al generar PDF para folio " + folio + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado al generar el PDF.", e);
        }
    }
}

