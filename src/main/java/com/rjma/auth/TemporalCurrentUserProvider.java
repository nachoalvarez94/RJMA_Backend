package com.rjma.auth;

import com.rjma.entity.Usuario;
import com.rjma.exception.ResourceNotFoundException;
import com.rjma.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemporalCurrentUserProvider implements CurrentUserProvider {

    private final UsuarioRepository usuarioRepository;

    @Value("${app.dev.current-user-username:vendedor1}")
    private String currentUserUsername;

    @Override
    public Usuario getCurrentUser() {
        return usuarioRepository.findByUsername(currentUserUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario activo no encontrado: " + currentUserUsername));
    }
}
