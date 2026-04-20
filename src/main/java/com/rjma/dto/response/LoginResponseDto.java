package com.rjma.dto.response;

import com.rjma.entity.Rol;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponseDto {

    String token;
    String tipo;        // "Bearer"
    long expiraEn;      // milisegundos desde epoch
    String username;
    String nombre;
    Rol rol;
}
