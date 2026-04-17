package com.rjma.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PedidoRequestDto {

    @NotNull
    private Long clienteId;

    private String observaciones;

    @DecimalMin("0.0")
    private BigDecimal importeCobrado;

    @NotEmpty
    @Valid
    private List<PedidoLineaRequestDto> lineas;
}
