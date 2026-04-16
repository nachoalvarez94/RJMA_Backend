package com.rjma.dto.response;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class ArticuloResponseDto {

    Long id;
    String nombre;
    BigDecimal precio;
    String codigoInterno;
    String codigoBarras;
    Boolean activo;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
