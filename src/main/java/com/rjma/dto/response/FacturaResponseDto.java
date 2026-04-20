package com.rjma.dto.response;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class FacturaResponseDto {

    Long id;
    Long numeroFactura;
    Long pedidoId;
    Long clienteId;
    String nombreCliente;
    String direccionCliente;
    String emailCliente;
    String telefonoCliente;
    String documentoFiscalCliente;
    LocalDateTime fechaEmision;
    String estado;
    BigDecimal baseImponible;
    BigDecimal impuestos;
    BigDecimal total;
    List<FacturaLineaResponseDto> lineas;
    String pdfPath;
    String pdfFileName;
    Integer pdfVersion;
    LocalDateTime pdfGeneratedAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
