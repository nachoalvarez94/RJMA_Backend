package com.rjma.dto.request;

import com.rjma.entity.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDto {

    @NotBlank(message = "El username es obligatorio")
    @Size(max = 100)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    private String nombre;

    @Size(max = 150)
    private String email;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;
}
