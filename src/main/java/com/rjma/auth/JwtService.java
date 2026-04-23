package com.rjma.auth;

import com.rjma.config.JwtProperties;
import com.rjma.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    // ── Generación ────────────────────────────────────────────────────────────

    public String generateToken(Usuario usuario) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getExpirationMs());

        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("rol", usuario.getRol().name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey())
                .compact();
    }

    // ── Validación y extracción ───────────────────────────────────────────────

    /**
     * Parsea el token y devuelve el username si es válido, o {@code null} si no lo es.
     * Un único parsing por request; el filtro no necesita llamar a isTokenValid + extractUsername por separado.
     */
    public String extractUsernameIfValid(String token) {
        try {
            return claims(token).getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    // ── Helpers privados ──────────────────────────────────────────────────────

    private Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signingKey() {
        // El secreto está en Base64 estándar (contiene '/' y '=='); no en Base64URL.
        // Decoders.BASE64URL rechaza '/' → usar Decoders.BASE64.
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
