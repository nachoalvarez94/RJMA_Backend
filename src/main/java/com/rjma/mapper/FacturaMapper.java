package com.rjma.mapper;

import com.rjma.dto.response.FacturaLineaResponseDto;
import com.rjma.dto.response.FacturaResponseDto;
import com.rjma.entity.Factura;
import com.rjma.entity.FacturaLinea;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FacturaMapper {

    public FacturaResponseDto toResponse(Factura factura, List<FacturaLinea> lineas) {
        return FacturaResponseDto.builder()
                .id(factura.getId())
                .numeroFactura(factura.getNumeroFactura())
                .pedidoId(factura.getPedido().getId())
                .clienteId(factura.getCliente().getId())
                .nombreCliente(factura.getNombreCliente())
                .direccionCliente(factura.getDireccionCliente())
                .emailCliente(factura.getEmailCliente())
                .telefonoCliente(factura.getTelefonoCliente())
                .documentoFiscalCliente(factura.getDocumentoFiscalCliente())
                .fechaEmision(factura.getFechaEmision())
                .estado(factura.getEstado())
                .baseImponible(factura.getBaseImponible())
                .impuestos(factura.getImpuestos())
                .total(factura.getTotal())
                .emitidaPorId(factura.getEmitidaPor() != null ? factura.getEmitidaPor().getId() : null)
                .lineas(lineas.stream().map(this::toResponseLinea).toList())
                .pdfPath(factura.getPdfPath())
                .pdfFileName(factura.getPdfFileName())
                .pdfVersion(factura.getPdfVersion())
                .pdfGeneratedAt(factura.getPdfGeneratedAt())
                .createdAt(factura.getCreatedAt())
                .updatedAt(factura.getUpdatedAt())
                .build();
    }

    public FacturaLineaResponseDto toResponseLinea(FacturaLinea linea) {
        return FacturaLineaResponseDto.builder()
                .id(linea.getId())
                .articuloId(linea.getArticulo() != null ? linea.getArticulo().getId() : null)
                .nombreArticulo(linea.getNombreArticulo())
                .codigoArticulo(linea.getCodigoArticulo())
                .precioUnitario(linea.getPrecioUnitario())
                .cantidad(linea.getCantidad())
                .subtotal(linea.getSubtotal())
                .tipoIva(linea.getTipoIva())
                .cuotaIva(linea.getCuotaIva())
                .totalLinea(linea.getTotalLinea())
                .build();
    }
}
