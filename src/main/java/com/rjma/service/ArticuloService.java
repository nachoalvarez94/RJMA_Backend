package com.rjma.service;

import com.rjma.dto.request.ArticuloRequestDto;
import com.rjma.dto.response.ArticuloResponseDto;
import com.rjma.entity.Articulo;
import com.rjma.exception.ResourceNotFoundException;
import com.rjma.mapper.ArticuloMapper;
import com.rjma.repository.ArticuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final ArticuloMapper articuloMapper;

    @Transactional
    public ArticuloResponseDto crear(ArticuloRequestDto dto) {
        Articulo articulo = articuloMapper.toEntity(dto);
        return articuloMapper.toResponse(articuloRepository.save(articulo));
    }

    @Transactional(readOnly = true)
    public ArticuloResponseDto obtenerPorId(Long id) {
        return articuloMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<ArticuloResponseDto> listarTodos() {
        return articuloRepository.findAll().stream()
                .map(articuloMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticuloResponseDto> buscarPorNombre(String nombre) {
        return articuloRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(articuloMapper::toResponse)
                .toList();
    }

    @Transactional
    public ArticuloResponseDto actualizar(Long id, ArticuloRequestDto dto) {
        Articulo articulo = findOrThrow(id);
        articuloMapper.updateEntity(articulo, dto);
        return articuloMapper.toResponse(articuloRepository.save(articulo));
    }

    @Transactional
    public void eliminar(Long id) {
        Articulo articulo = findOrThrow(id);
        articulo.setActivo(false);
        articuloRepository.save(articulo);
    }

    @Transactional
    public ArticuloResponseDto activar(Long id) {
        Articulo articulo = findOrThrow(id);
        articulo.setActivo(true);
        return articuloMapper.toResponse(articuloRepository.save(articulo));
    }

    @Transactional
    public ArticuloResponseDto desactivar(Long id) {
        Articulo articulo = findOrThrow(id);
        articulo.setActivo(false);
        return articuloMapper.toResponse(articuloRepository.save(articulo));
    }

    @Transactional(readOnly = true)
    public List<ArticuloResponseDto> buscarAdmin(String nombre, Boolean activo) {
        List<Articulo> result;
        if (nombre != null && activo != null) {
            result = articuloRepository.findByActivoAndNombreContainingIgnoreCase(activo, nombre);
        } else if (nombre != null) {
            result = articuloRepository.findByNombreContainingIgnoreCase(nombre);
        } else if (activo != null) {
            result = articuloRepository.findByActivo(activo);
        } else {
            result = articuloRepository.findAll();
        }
        return result.stream().map(articuloMapper::toResponse).toList();
    }

    private Articulo findOrThrow(Long id) {
        return articuloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artículo no encontrado: " + id));
    }
}
