package com.rjma.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtProperties {

    /**
     * Clave HMAC-SHA256 codificada en Base64 estándar (mínimo 256 bits / 32 bytes en crudo).
     * Generarla con: openssl rand -base64 32
     * Cambiar obligatoriamente en producción; usar variable de entorno APP_JWT_SECRET.
     */
    private String secret;

    /**
     * Tiempo de vida del token en milisegundos. Por defecto 24 h.
     */
    private long expirationMs = 86_400_000L;
}
