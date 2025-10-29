package com.javhlahm.acbmin_autoalmacen.service;

import com.javhlahm.acbmin_autoalmacen.entity.Resguardo;
import com.javhlahm.acbmin_autoalmacen.repository.ResguardoRepo;
import jakarta.persistence.EntityNotFoundException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ResguardoServicio {

    @Autowired
    private ResguardoRepo resguardoRepo;

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


    // --- Métodos CRUD (sin cambios) ---
    public List<Resguardo> getAllResguardos() { 
        return resguardoRepo.findAll(); }

    public Optional<Resguardo> getResguardoByFolio(Integer folio) {
         return resguardoRepo.findById(folio); }

    @Transactional public Resguardo createResguardo(Resguardo resguardo) { 
        if (resguardo.getFolio() != null) { 
            resguardo.setFolio(null); } 
        return resguardoRepo.save(resguardo); }

    @Transactional public void deleteResguardo(Integer folio) { 
        Resguardo resguardoExistente = resguardoRepo.findById(folio).orElseThrow(() -> new EntityNotFoundException("Resguardo no encontrado con folio: " + folio)); 
        resguardoRepo.delete(resguardoExistente); }
        
    @Transactional
    public Resguardo updateResguardo(Integer folio, Resguardo resguardoDetails) {
        Resguardo resguardoExistente = resguardoRepo.findById(folio)
                .orElseThrow(() -> new EntityNotFoundException("Resguardo no encontrado con folio: " + folio));

        String estatusNuevo = resguardoDetails.getEstatus();
        String estatusViejo = resguardoExistente.getEstatus();
        boolean estatusCambiado = estatusNuevo != null && !estatusNuevo.equalsIgnoreCase(estatusViejo);

        if (estatusCambiado) {
            resguardoExistente.setEstatus(estatusNuevo);
            if ("Aprobado".equalsIgnoreCase(estatusNuevo)) {
                resguardoExistente.setFechaAutorizado(LocalDateTime.now());
            } else {
                resguardoExistente.setFechaAutorizado(null);
            }
        }

        // Actualizar otros campos...
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
        resguardoExistente.setCapturadoPor(resguardoDetails.getCapturadoPor());

        return resguardoRepo.save(resguardoExistente);
    }
    // --- Fin Métodos CRUD ---


    // --- Método para generar PDF ---
    public byte[] generarPdfResguardo(Integer folio) throws JRException, EntityNotFoundException, IOException {
        Resguardo resguardo = resguardoRepo.findById(folio)
                .orElseThrow(() -> new EntityNotFoundException("Resguardo no encontrado con folio: " + folio));

        String reportPath = "classpath:reports/Resguardo_interno.jasper";
        InputStream reportStream = resourceLoader.getResource(reportPath).getInputStream();
        if (reportStream == null) {
            throw new IOException("No se pudo encontrar la plantilla del reporte en: " + reportPath);
        }
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("P_FOLIO", "RICB-" + String.format("%04d", resguardo.getFolio()));
        parameters.put("P_FECHA", safeDate(resguardo.getFechaAutorizado()));
        parameters.put("P.INVENTARIO", safeString(resguardo.getNumeroInventario()));
        parameters.put("P.DESCRIPCION", safeString(resguardo.getDescripcion()));
        parameters.put("P.AREAENTREGA", safeString(resguardo.getAreaEntrega()));
        parameters.put("P.NOMBREENTREGA", safeString(resguardo.getNombreEntrega()));
        parameters.put("P.RFCENTREGA", safeString(resguardo.getRfcEntrega()));
        parameters.put("P.AREARECIBE", safeString(resguardo.getAreaRecibe()));
        parameters.put("P.NOMBRERECIBE", safeString(resguardo.getNombreRecibe()));
        parameters.put("P.RFCRECIBE", safeString(resguardo.getRfcRecibe()));
        parameters.put("P.OBSERVACIONES", safeString(resguardo.getObservaciones()));

        // --- Lógica para Checkboxes P_NUEVO y P_TRANSFERENCIA ---
        String tipo = safeString(resguardo.getTipoResguardo()).toLowerCase(); // Obtener tipo en minúsculas
        if (tipo.contains("nueva adquisición")) { // Comprobar si contiene la frase clave
            parameters.put("P.NUEVO", "X");
            parameters.put("P.TRANSFERENCIA", ""); // Vacío si es nuevo
        } else if (tipo.contains("traspaso")) { // Comprobar si contiene la frase clave
            parameters.put("P.NUEVO", ""); // Vacío si es traspaso
            parameters.put("P.TRANSFERENCIA", "X");
        } else {
            // Caso por defecto o si el tipo no coincide con ninguno esperado
            parameters.put("P.NUEVO", "");
            parameters.put("P.TRANSFERENCIA", "");
             System.err.println("Advertencia: Tipo de resguardo no reconocido ('" + tipo + "') para folio " + folio + ". Checkboxes quedarán vacíos.");
        }
        // --- Fin Lógica Checkboxes ---


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

        JRDataSource dataSource = new JREmptyDataSource();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Cerrar el InputStream del logo si se abrió
        if (logoInputStream != null) {
             try {
                 logoInputStream.close();
             } catch (IOException e) {
                 System.err.println("Advertencia: No se pudo cerrar el InputStream del logo: " + e.getMessage());
             }
         }

        return JasperExportManager.exportReportToPdf(jasperPrint);
    } // <-- Cierre del método generarPdfResguardo

} // <-- Cierre de la clase ResguardoServicio

