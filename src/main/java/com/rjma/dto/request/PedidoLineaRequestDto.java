package com.rjma.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoLineaRequestDto {

    @NotNull
    private Long articuloId;

    /** Puede ser negativo (devolución). Validación de rango y escala en PedidoService. */
    @NotNull
    private BigDecimal cantidad;
}
