package com.rjma.dto.response;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class FacturaLineaResponseDto {

    Long id;
    Long articuloId;
    String nombreArticulo;
    String codigoArticulo;
    BigDecimal precioUnitario;
    BigDecimal cantidad;
    BigDecimal subtotal;
    BigDecimal tipoIva;
    BigDecimal cuotaIva;
    BigDecimal totalLinea;
}
