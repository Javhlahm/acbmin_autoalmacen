package com.javhlahm.acbmin_autoalmacen.service;

import com.javhlahm.acbmin_autoalmacen.entity.Resguardo;
import com.javhlahm.acbmin_autoalmacen.repository.ResguardoRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResguardoServicio {

    @Autowired
    private ResguardoRepo resguardoRepo;

    public List<Resguardo> getAllResguardos() {
        return resguardoRepo.findAll(); // O considera usar findAllByOrderByFolioAsc() si quieres un orden
    }

    public Optional<Resguardo> getResguardoByFolio(Integer folio) {
        return resguardoRepo.findById(folio);
    }

    @Transactional // Asegura atomicidad
    public Resguardo createResguardo(Resguardo resguardo) {
        // Se confía en que el frontend envía los datos iniciales (estatus="Pendiente", fechaAutorizado=null, capturadoPor)
        // y que la BD asignará el folio auto-incremental al guardar.
        // Asegurarse de que el folio sea null para que funcione el AUTO_INCREMENT
        if (resguardo.getFolio() != null) {
            System.out.println("WARN: Folio recibido en createResguardo, forzando a null para auto-generación.");
            resguardo.setFolio(null);
        }
        return resguardoRepo.save(resguardo);
    }

    @Transactional
    public Resguardo updateResguardo(Integer folio, Resguardo resguardoDetails) {
        Resguardo resguardoExistente = resguardoRepo.findById(folio)
                .orElseThrow(() -> new EntityNotFoundException("Resguardo no encontrado con folio: " + folio));

        // --- Lógica de Estatus integrada ---
        String estatusNuevo = resguardoDetails.getEstatus();
        String estatusViejo = resguardoExistente.getEstatus();

        // Compara ignorando mayúsculas/minúsculas por si acaso
        boolean estatusCambiado = estatusNuevo != null && !estatusNuevo.equalsIgnoreCase(estatusViejo);

        if (estatusCambiado) {
            resguardoExistente.setEstatus(estatusNuevo); // Actualiza estatus
            if ("Aprobado".equalsIgnoreCase(estatusNuevo)) {
                // Si el NUEVO estatus es Aprobado, poner fecha actual
                resguardoExistente.setFechaAutorizado(LocalDateTime.now());
            } else {
                // Si el NUEVO estatus NO es Aprobado (Pendiente, Rechazado, etc.), limpiar fecha
                resguardoExistente.setFechaAutorizado(null);
            }
        }
        // --- Fin Lógica de Estatus ---

        // Actualizar otros campos (excepto folio que es la clave)
        // Se asume que resguardoDetails viene con todos los campos necesarios, incluido capturadoPor
        resguardoExistente.setTipoResguardo(resguardoDetails.getTipoResguardo());
        resguardoExistente.setNumeroInventario(resguardoDetails.getNumeroInventario());
        resguardoExistente.setDescripcion(resguardoDetails.getDescripcion());
        resguardoExistente.setAreaEntrega(resguardoDetails.getAreaEntrega());
        resguardoExistente.setNombreEntrega(resguardoDetails.getNombreEntrega());
        resguardoExistente.setRfcEntrega(resguardoDetails.getRfcEntrega());
        resguardoExistente.setAreaRecibe(resguardoDetails.getAreaRecibe());
        resguardoExistente.setNombreRecibe(resguardoDetails.getNombreRecibe());
        resguardoExistente.setRfcRecibe(resguardoDetails.getRfcRecibe());
        resguardoExistente.setObservaciones(resguardoDetails.getObservaciones());
        resguardoExistente.setCapturadoPor(resguardoDetails.getCapturadoPor()); // Actualiza quién hizo el último cambio

        return resguardoRepo.save(resguardoExistente);
    }


    @Transactional
    public void deleteResguardo(Integer folio) {
        Resguardo resguardoExistente = resguardoRepo.findById(folio)
                .orElseThrow(() -> new EntityNotFoundException("Resguardo no encontrado con folio: " + folio));
        resguardoRepo.delete(resguardoExistente);
    }

}

