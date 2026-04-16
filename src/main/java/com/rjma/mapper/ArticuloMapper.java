package com.rjma.mapper;

import com.rjma.dto.request.ArticuloRequestDto;
import com.rjma.dto.response.ArticuloResponseDto;
import com.rjma.entity.Articulo;
import org.springframework.stereotype.Component;

@Component
public class ArticuloMapper {

    public ArticuloResponseDto toResponse(Articulo articulo) {
        return ArticuloResponseDto.builder()
                .id(articulo.getId())
                .nombre(articulo.getNombre())
                .precio(articulo.getPrecio())
                .codigoInterno(articulo.getCodigoInterno())
                .codigoBarras(articulo.getCodigoBarras())
                .activo(articulo.getActivo())
                .createdAt(articulo.getCreatedAt())
                .updatedAt(articulo.getUpdatedAt())
                .build();
    }

    public Articulo toEntity(ArticuloRequestDto dto) {
        return Articulo.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .codigoInterno(dto.getCodigoInterno())
                .codigoBarras(dto.getCodigoBarras())
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();
    }

    public void updateEntity(Articulo articulo, ArticuloRequestDto dto) {
        articulo.setNombre(dto.getNombre());
        articulo.setPrecio(dto.getPrecio());
        articulo.setCodigoInterno(dto.getCodigoInterno());
        articulo.setCodigoBarras(dto.getCodigoBarras());
        if (dto.getActivo() != null) {
            articulo.setActivo(dto.getActivo());
        }
    }
}
