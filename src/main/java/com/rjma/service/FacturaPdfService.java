package com.rjma.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.rjma.config.InvoiceStorageProperties;
import com.rjma.entity.Factura;
import com.rjma.entity.FacturaLinea;
import com.rjma.exception.BadRequestException;
import com.rjma.exception.PdfGenerationException;
import com.rjma.exception.ResourceNotFoundException;
import com.rjma.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaPdfService {

    private final InvoiceStorageProperties storageProperties;
    private final FacturaRepository facturaRepository;

    @Transactional
    public void generarYGuardar(Factura factura, List<FacturaLinea> lineas) {
        try {
            LocalDateTime fechaRef = factura.getFechaEmision() != null
                    ? factura.getFechaEmision()
                    : LocalDateTime.now();

            String year = String.valueOf(fechaRef.getYear());
            String month = String.format("%02d", fechaRef.getMonthValue());

            Path directory = Path.of(storageProperties.getStoragePath(), year, month);
            Files.createDirectories(directory);

            // Calcular versión combinando BD y filesystem para evitar sobrescrituras
            int version = calcularSiguienteVersion(directory, factura.getNumeroFactura(), factura.getPdfVersion());

            String fileName = String.format("FAC-%06d-v%d.pdf", factura.getNumeroFactura(), version);
            Path filePath = directory.resolve(fileName);

            byte[] pdfBytes = generarPdfBytes(factura, lineas);
            Files.write(filePath, pdfBytes);

            // Guardar ruta relativa en BD (portable entre entornos y Docker)
            String relativePath = year + "/" + month + "/" + fileName;

            factura.setPdfPath(relativePath);
            factura.setPdfFileName(fileName);
            factura.setPdfVersion(version);
            factura.setPdfGeneratedAt(LocalDateTime.now());
            facturaRepository.save(factura);

        } catch (IOException | DocumentException e) {
            throw new PdfGenerationException("Error al generar el PDF de la factura: " + factura.getNumeroFactura(), e);
        }
    }

    public ResponseEntity<Resource> descargar(Long facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada: " + facturaId));

        if (factura.getPdfPath() == null) {
            throw new BadRequestException("La factura " + facturaId + " no tiene PDF generado");
        }

        // Resolver ruta absoluta desde la relativa almacenada en BD
        Path filePath = Path.of(storageProperties.getStoragePath()).resolve(factura.getPdfPath());

        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException(
                    "El archivo PDF no existe en disco: " + factura.getPdfFileName());
        }

        try {
            long fileSize = Files.size(filePath);
            InputStream inputStream = Files.newInputStream(filePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(fileSize)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.attachment()
                                    .filename(factura.getPdfFileName())
                                    .build()
                                    .toString())
                    .body(new InputStreamResource(inputStream));

        } catch (IOException e) {
            throw new PdfGenerationException("Error al leer el PDF de la factura: " + facturaId, e);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Calcula la siguiente versión disponible combinando el valor de BD y el filesystem.
     * Nunca sobrescribe un archivo existente, incluso si la BD está desincronizada.
     */
    private int calcularSiguienteVersion(Path directory, Long numeroFactura, Integer pdfVersionBD) {
        int version = pdfVersionBD != null ? pdfVersionBD + 1 : 1;
        while (Files.exists(directory.resolve(
                String.format("FAC-%06d-v%d.pdf", numeroFactura, version)))) {
            version++;
        }
        return version;
    }

    // ── PDF generation ────────────────────────────────────────────────────────

    private byte[] generarPdfBytes(Factura factura, List<FacturaLinea> lineas) throws DocumentException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 60, 60);
        PdfWriter.getInstance(document, out);
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font fontSmall = FontFactory.getFont(FontFactory.HELVETICA, 9);

        // ── Cabecera ──────────────────────────────────────────────────────────
        document.add(new Paragraph("FACTURA", fontTitle));
        document.add(new Paragraph(
                String.format("Nº %06d", factura.getNumeroFactura()), fontHeader));
        document.add(new Paragraph(
                "Fecha: " + factura.getFechaEmision().toLocalDate(), fontNormal));
        document.add(Chunk.NEWLINE);

        // ── Datos del cliente ─────────────────────────────────────────────────
        document.add(new Paragraph("Cliente", fontHeader));
        document.add(new Paragraph(factura.getNombreCliente(), fontNormal));
        if (factura.getDireccionCliente() != null) {
            document.add(new Paragraph(factura.getDireccionCliente(), fontNormal));
        }
        if (factura.getDocumentoFiscalCliente() != null) {
            document.add(new Paragraph("NIF/CIF: " + factura.getDocumentoFiscalCliente(), fontNormal));
        }
        if (factura.getTelefonoCliente() != null) {
            document.add(new Paragraph("Tel: " + factura.getTelefonoCliente(), fontNormal));
        }
        document.add(Chunk.NEWLINE);

        // ── Tabla de líneas ───────────────────────────────────────────────────
        PdfPTable tabla = new PdfPTable(new float[]{4f, 1.5f, 2f, 1.5f, 2f});
        tabla.setWidthPercentage(100);

        addHeaderCell(tabla, "Artículo", fontHeader);
        addHeaderCell(tabla, "Cantidad", fontHeader);
        addHeaderCell(tabla, "Precio unit.", fontHeader);
        addHeaderCell(tabla, "IVA %", fontHeader);
        addHeaderCell(tabla, "Total línea", fontHeader);

        for (FacturaLinea linea : lineas) {
            addCell(tabla, linea.getNombreArticulo(), fontSmall);
            addCell(tabla, linea.getCantidad().toPlainString(), fontSmall);
            addCell(tabla, linea.getPrecioUnitario().toPlainString() + " €", fontSmall);
            addCell(tabla, linea.getTipoIva().toPlainString() + " %", fontSmall);
            addCell(tabla, linea.getTotalLinea().toPlainString() + " €", fontSmall);
        }

        document.add(tabla);
        document.add(Chunk.NEWLINE);

        // ── Totales ───────────────────────────────────────────────────────────
        PdfPTable totales = new PdfPTable(new float[]{3f, 1.5f});
        totales.setWidthPercentage(50);
        totales.setHorizontalAlignment(Element.ALIGN_RIGHT);

        addCell(totales, "Base imponible", fontNormal);
        addCell(totales, factura.getBaseImponible().toPlainString() + " €", fontNormal);
        addCell(totales, "IVA (21%)", fontNormal);
        addCell(totales, factura.getImpuestos().toPlainString() + " €", fontNormal);
        addCell(totales, "TOTAL", fontHeader);
        addCell(totales, factura.getTotal().toPlainString() + " €", fontHeader);

        document.add(totales);
        document.close();

        return out.toByteArray();
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(new java.awt.Color(220, 220, 220));
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(4);
        table.addCell(cell);
    }
}
