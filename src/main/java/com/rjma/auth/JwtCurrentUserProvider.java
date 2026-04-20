package com.rjma.auth;

import com.rjma.entity.Usuario;
import com.rjma.exception.ResourceNotFoundException;
import com.rjma.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class JwtCurrentUserProvider implements CurrentUserProvider {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new ResourceNotFoundException("No hay sesión autenticada");
        }

        String username = auth.getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario autenticado no encontrado en base de datos: " + username));
    }
}
