package com.rjma.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PedidoRequestDto {

    @NotNull
    private Long clienteId;

    private String observaciones;

    private String estado;

    @NotEmpty
    @Valid
    private List<PedidoLineaRequestDto> lineas;
}
