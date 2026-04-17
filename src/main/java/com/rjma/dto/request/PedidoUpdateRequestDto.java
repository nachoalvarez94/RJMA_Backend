package com.rjma.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PedidoUpdateRequestDto {

    private String observaciones;

    @DecimalMin("0.0")
    private BigDecimal importeCobrado;

    @NotEmpty
    @Valid
    private List<PedidoLineaUpdateRequestDto> lineas;
}
