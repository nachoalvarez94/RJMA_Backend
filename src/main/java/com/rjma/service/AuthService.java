package com.rjma.service;

import com.rjma.auth.JwtService;
import com.rjma.config.JwtProperties;
import com.rjma.dto.request.LoginRequestDto;
import com.rjma.dto.response.LoginResponseDto;
import com.rjma.entity.Usuario;
import com.rjma.exception.BadRequestException;
import com.rjma.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Credenciales incorrectas");
        }

        // Si llegamos aquí, la autenticación fue exitosa
        Usuario usuario = usuarioRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        String token = jwtService.generateToken(usuario);
        long expiraEn = System.currentTimeMillis() + jwtProperties.getExpirationMs();

        return LoginResponseDto.builder()
                .token(token)
                .tipo("Bearer")
                .expiraEn(expiraEn)
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .build();
    }
}
