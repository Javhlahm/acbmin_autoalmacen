package com.javhlahm.acbmin_autoalmacen.service;

import com.javhlahm.acbmin_autoalmacen.entity.BajaBien;
import com.javhlahm.acbmin_autoalmacen.repository.BajaBienRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BajaBienServicio {

    @Autowired
    private BajaBienRepo bajaBienRepo;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final DateTimeFormatter PDF_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // --- Métodos Helper Privados ---
    private String safeString(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    private String safeDate(LocalDateTime dt) {
        return dt != null ? dt.format(PDF_DATE_FORMATTER) : "";
    }

    // --- Fin Métodos Helper ---
    // Crear una nueva baja
    public BajaBien crearBaja(BajaBien bajaBien) {
        // Validaciones básicas (puedes añadir más)
        if (bajaBien.getEstatus() == null || bajaBien.getEstatus().isEmpty()) {
            bajaBien.setEstatus("Pendiente"); // Establecer estatus inicial si no viene
        }
        // Aquí podrías validar que no exista ya un número de inventario similar, etc.
        return bajaBienRepo.save(bajaBien);
    }

    // Obtener todas las bajas
    public List<BajaBien> obtenerTodasLasBajas() {
        return bajaBienRepo.findAll();
    }

    // Obtener una baja por su folio
    public Optional<BajaBien> obtenerBajaPorFolio(int folio) {
        return bajaBienRepo.findById(folio);
    }

    // Actualizar una baja existente
    public Optional<BajaBien> actualizarBaja(int folio, BajaBien bajaActualizada) {
        return bajaBienRepo.findById(folio).map(bajaExistente -> {
            // Actualizar campos (asegúrate de incluir todos los campos del frontend)
            bajaExistente.setAreaBaja(bajaActualizada.getAreaBaja());
            bajaExistente.setPersonaBaja(bajaActualizada.getPersonaBaja());
            bajaExistente.setRfcBaja(bajaActualizada.getRfcBaja());
            bajaExistente.setDescripcion(bajaActualizada.getDescripcion());
            bajaExistente.setNumeroInventario(bajaActualizada.getNumeroInventario());
            bajaExistente.setFechaEntregaActivos(bajaActualizada.getFechaEntregaActivos());
            bajaExistente.setCapturadoPor(bajaActualizada.getCapturadoPor()); // Quien modifica
            bajaExistente.setObservaciones(bajaActualizada.getObservaciones()); // Quien modifica

            // Lógica para actualizar estatus y fecha autorizado (similar a
            // ResguardoServicio)
            if (bajaActualizada.getEstatus() != null
                    && !bajaActualizada.getEstatus().equals(bajaExistente.getEstatus())) {
                // Si el estatus está cambiando a Aprobado y antes no lo era
                if ("Aprobado".equalsIgnoreCase(bajaActualizada.getEstatus())
                        && !"Aprobado".equalsIgnoreCase(bajaExistente.getEstatus())) {
                    bajaExistente.setFechaAutorizado(LocalDateTime.now()); // Establecer fecha actual
                }
                // Si el estatus cambia a algo diferente de Aprobado, limpiar fecha
                else if (!"Aprobado".equalsIgnoreCase(bajaActualizada.getEstatus())) {
                    bajaExistente.setFechaAutorizado(null);
                }
                bajaExistente.setEstatus(bajaActualizada.getEstatus());
            }

            return bajaBienRepo.save(bajaExistente);
        });
    }

    // Eliminar una baja por folio
    public boolean eliminarBaja(int folio) {
        if (bajaBienRepo.existsById(folio)) {
            bajaBienRepo.deleteById(folio);
            return true;
        }
        return false;
    }

    // Generar reporte PDF para una baja (similar a ResguardoServicio)
    public byte[] exportReportBaja(int folio) throws FileNotFoundException, JRException {
        Optional<BajaBien> bajaOpt = bajaBienRepo.findById(folio);
        if (bajaOpt.isEmpty()) {
            throw new RuntimeException("Baja con folio " + folio + " no encontrada.");
        }

        BajaBien baja = bajaOpt.get();

        // Cargar archivo .jasper (asumiendo que tienes Baja_interna.jasper)
        File file = ResourceUtils.getFile("classpath:reports/Baja_interna.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file);

        JRDataSource dataSource = new JREmptyDataSource();

        // Parámetros del reporte (si los necesitas, ej. imágenes)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("P_FOLIO", "BICB-" + String.format("%04d", baja.getFolio()));
        parameters.put("P_FECHA", safeDate(baja.getFechaAutorizado()));
        parameters.put("P.INVENTARIO", safeString(baja.getNumeroInventario()));
        parameters.put("P.DESCRIPCION", safeString(baja.getDescripcion()));
        parameters.put("P.AREAENTREGA", safeString(baja.getAreaBaja()));
        parameters.put("P.NOMBREENTREGA", safeString(baja.getPersonaBaja()));
        parameters.put("P.RFCENTREGA", safeString(baja.getRfcBaja()));
        parameters.put("P.OBSERVACIONES", safeString(baja.getObservaciones()));

        // Cargar y añadir el logo como parámetro
        String logoPath = "classpath:img/logo-Educacion-1.png";
        InputStream logoInputStream = null;
        try {
            Resource logoResource = resourceLoader.getResource(logoPath);
            if (logoResource.exists()) {
                logoInputStream = logoResource.getInputStream();
                parameters.put("P.LOGO", logoInputStream); // *** USA EL NOMBRE DE TU PARÁMETRO ***
                System.out.println("Logo cargado exitosamente desde: " + logoPath);
            } else {
                System.err.println("Advertencia: No se encontró el archivo del logo en: " + logoPath);
            }
        } catch (IOException e) {
            System.err.println("Error al intentar cargar el logo desde " + logoPath + ": " + e.getMessage());
        }

        // Llenar el reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Cerrar el InputStream del logo si se abrió
        if (logoInputStream != null) {
            try {
                logoInputStream.close();
            } catch (IOException e) {
                System.err.println("Advertencia: No se pudo cerrar el InputStream del logo: " + e.getMessage());
            }
        }
        // Exportar a PDF y devolver los bytes
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}