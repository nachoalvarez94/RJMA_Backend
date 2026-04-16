package com.rjma.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ClienteResponseDto {

    Long id;
    String nombre;
    String direccion;
    String email;
    String telefono;
    String nombreComercio;
    String poblacion;
    String municipio;
    String documentoFiscal;
    Boolean activo;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
