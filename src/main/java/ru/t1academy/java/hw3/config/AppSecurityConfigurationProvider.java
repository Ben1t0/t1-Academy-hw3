package ru.t1academy.java.hw3.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Getter
@Component
public class AppSecurityConfigurationProvider implements JwtSecretKeyProvider, JwtConfigurationProvider {
    private final SecretKey jwtAccessSecret;

    private final SecretKey jwtRefreshSecret;

    private final long jwtAccessExpirationMs;

    private final long jwtRefreshExpirationMs;

    public AppSecurityConfigurationProvider(
            @Value("${app.security.jwt.secret.hs_256_access}") String jwtAccessSecret,
            @Value("${app.security.jwt.secret.hs_256_refresh}") String jwtRefreshSecret,
            @Value("${app.security.jwt.accessExpirationMs}") long jwtAccessExpirationMs,
            @Value("${app.security.jwt.refreshExpirationMs}") long jwtRefreshExpirationMs) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.jwtAccessExpirationMs = jwtAccessExpirationMs;
        this.jwtRefreshExpirationMs = jwtRefreshExpirationMs;
    }
}
