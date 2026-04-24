package com.rjma.mapper;

import com.rjma.dto.request.ArticuloRequestDto;
import com.rjma.dto.response.ArticuloResponseDto;
import com.rjma.entity.Articulo;
import com.rjma.entity.UnidadVenta;
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
                // Registros anteriores a la migración tienen null → devolver UNIDAD
                .unidadVenta(articulo.getUnidadVenta() != null
                        ? articulo.getUnidadVenta()
                        : UnidadVenta.UNIDAD)
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
                .unidadVenta(dto.getUnidadVenta() != null ? dto.getUnidadVenta() : UnidadVenta.UNIDAD)
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();
    }

    public void updateEntity(Articulo articulo, ArticuloRequestDto dto) {
        articulo.setNombre(dto.getNombre());
        articulo.setPrecio(dto.getPrecio());
        articulo.setCodigoInterno(dto.getCodigoInterno());
        articulo.setCodigoBarras(dto.getCodigoBarras());
        // Solo actualiza unidadVenta si se envía explícitamente; conserva el valor existente si no
        if (dto.getUnidadVenta() != null) {
            articulo.setUnidadVenta(dto.getUnidadVenta());
        }
        if (dto.getActivo() != null) {
            articulo.setActivo(dto.getActivo());
        }
    }
}
