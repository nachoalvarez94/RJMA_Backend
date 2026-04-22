package com.rjma.dto.response;

import com.rjma.entity.Rol;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class UsuarioResponseDto {

    Long id;
    String username;
    String nombre;
    String email;
    Boolean activo;
    Rol rol;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
