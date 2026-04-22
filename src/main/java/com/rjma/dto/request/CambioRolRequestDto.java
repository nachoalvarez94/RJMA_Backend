package com.rjma.dto.request;

import com.rjma.entity.Rol;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CambioRolRequestDto {

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;
}
