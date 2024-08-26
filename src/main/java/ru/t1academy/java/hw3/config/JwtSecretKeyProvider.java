package ru.t1academy.java.hw3.config;

import javax.crypto.SecretKey;

public interface JwtSecretKeyProvider {
    SecretKey getJwtAccessSecret();

    SecretKey getJwtRefreshSecret();
}
