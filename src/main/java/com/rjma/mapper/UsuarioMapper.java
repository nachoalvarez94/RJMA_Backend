package com.rjma.mapper;

import com.rjma.dto.response.UsuarioResponseDto;
import com.rjma.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponseDto toResponse(Usuario usuario) {
        return UsuarioResponseDto.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .activo(usuario.getActivo())
                .rol(usuario.getRol())
                .createdAt(usuario.getCreatedAt())
                .updatedAt(usuario.getUpdatedAt())
                .build();
    }
}
