package com.rjma.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ArticuloRequestDto {

    @NotBlank
    @Size(max = 150)
    private String nombre;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal precio;

    @Size(max = 50)
    private String codigoInterno;

    @Size(max = 50)
    private String codigoBarras;

    private Boolean activo;
}
