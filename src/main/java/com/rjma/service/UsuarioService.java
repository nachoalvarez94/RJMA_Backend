package com.rjma.service;

import com.rjma.dto.request.CambioRolRequestDto;
import com.rjma.dto.request.UsuarioRequestDto;
import com.rjma.dto.request.UsuarioUpdateRequestDto;
import com.rjma.dto.response.UsuarioResponseDto;
import com.rjma.entity.Usuario;
import com.rjma.exception.BadRequestException;
import com.rjma.exception.ResourceNotFoundException;
import com.rjma.mapper.UsuarioMapper;
import com.rjma.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDto obtenerPorId(Long id) {
        return usuarioMapper.toResponse(findOrThrow(id));
    }

    @Transactional
    public UsuarioResponseDto crear(UsuarioRequestDto dto) {
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new BadRequestException("Ya existe un usuario con el username: " + dto.getUsername());
        }

        Usuario usuario = Usuario.builder()
                .username(dto.getUsername())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .rol(dto.getRol())
                .build();

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDto actualizar(Long id, UsuarioUpdateRequestDto dto) {
        Usuario usuario = findOrThrow(id);
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDto cambiarRol(Long id, CambioRolRequestDto dto) {
        Usuario usuario = findOrThrow(id);
        usuario.setRol(dto.getRol());
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDto activar(Long id) {
        Usuario usuario = findOrThrow(id);
        usuario.setActivo(true);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDto desactivar(Long id) {
        Usuario usuario = findOrThrow(id);
        usuario.setActivo(false);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    private Usuario findOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
    }
}
