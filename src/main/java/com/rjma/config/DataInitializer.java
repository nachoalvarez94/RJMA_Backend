package com.rjma.config;

import com.rjma.entity.Rol;
import com.rjma.entity.Usuario;
import com.rjma.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Crea los usuarios iniciales en la base de datos si no existen.
 * Se ejecuta en cada arranque; es idempotente gracias al findByUsername previo.
 *
 * Contraseñas por defecto (cambiar en producción):
 *   vendedor1 → "1234"
 *   admin     → "admin1234"
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        crearSiNoExiste("vendedor1", "1234", "Vendedor Uno", "vendedor1@rjma.com", Rol.VENDEDOR);
        crearSiNoExiste("vendedor2", "1234", "Vendedor Dis", "vendedor2@rjma.com", Rol.VENDEDOR);
        crearSiNoExiste("admin", "admin1234", "Administrador", "admin@rjma.com", Rol.ADMIN);
    }

    private void crearSiNoExiste(String username, String password,
                                  String nombre, String email, Rol rol) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            return;
        }

        Usuario usuario = Usuario.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(password))
                .nombre(nombre)
                .email(email)
                .rol(rol)
                .build();

        usuarioRepository.save(usuario);
        log.info("Usuario creado: {} ({})", username, rol);
    }
}
