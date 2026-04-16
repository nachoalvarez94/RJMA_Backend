package com.rjma.dto.response;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class PedidoResponseDto {

    Long id;
    Long numero;
    Long clienteId;
    LocalDateTime fecha;
    String estado;
    String observaciones;
    BigDecimal totalBruto;
    BigDecimal totalDescuento;
    BigDecimal totalFinal;
    List<PedidoLineaResponseDto> lineas;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
