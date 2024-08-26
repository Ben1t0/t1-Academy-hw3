package ru.t1academy.java.hw3.service.authorization;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.t1academy.java.hw3.config.JwtConfigurationProvider;
import ru.t1academy.java.hw3.config.JwtSecretKeyProvider;
import ru.t1academy.java.hw3.model.RefreshToken;
import ru.t1academy.java.hw3.model.UserDetailsImpl;
import ru.t1academy.java.hw3.repository.RefreshTokenRepository;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtConfigurationProvider config;

    private final JwtSecretKeyProvider keys;

    private final RefreshTokenRepository refreshTokenRepository;

    public String generateAccessToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + config.getJwtAccessExpirationMs()))
                .signWith(keys.getJwtAccessSecret())
                .claim("id", ((UserDetailsImpl) user).getId())
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        Date expiresAt = new Date((new Date()).getTime() + config.getJwtRefreshExpirationMs());
        String refreshToken = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiresAt)
                .signWith(keys.getJwtRefreshSecret())
                .compact();
        refreshTokenRepository.save(new RefreshToken(user.getUsername(), refreshToken, expiresAt));
        return refreshToken;
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, keys.getJwtAccessSecret());
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, keys.getJwtRefreshSecret());
    }

    private boolean validateToken(String token, Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Jwt token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Jwt token is unsupported: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Invalid signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Jwt claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, keys.getJwtAccessSecret());
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, keys.getJwtRefreshSecret());
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
