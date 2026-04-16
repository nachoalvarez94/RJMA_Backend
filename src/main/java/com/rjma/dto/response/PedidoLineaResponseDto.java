package com.rjma.dto.response;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class PedidoLineaResponseDto {

    Long id;
    Long articuloId;
    String nombreArticulo;
    BigDecimal precioUnitario;
    BigDecimal cantidad;
    BigDecimal subtotal;
    BigDecimal descuento;
    BigDecimal totalLinea;
}
