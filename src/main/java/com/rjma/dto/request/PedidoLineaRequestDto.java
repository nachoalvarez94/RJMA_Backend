package com.rjma.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoLineaRequestDto {

    @NotNull
    private Long articuloId;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal cantidad;
}
