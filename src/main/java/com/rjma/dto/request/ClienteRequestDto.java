package com.rjma.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteRequestDto {

    @NotBlank
    @Size(max = 150)
    private String nombre;

    @NotBlank
    @Size(max = 255)
    private String direccion;

    @Email
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(max = 20)
    private String telefono;

    @Size(max = 150)
    private String nombreComercio;

    @Size(max = 100)
    private String poblacion;

    @Size(max = 100)
    private String municipio;

    @Size(max = 20)
    private String documentoFiscal;

    private Boolean activo;
}
