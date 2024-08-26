package ru.t1academy.java.hw3.config;

public interface JwtConfigurationProvider {
    long getJwtAccessExpirationMs();

    long getJwtRefreshExpirationMs();
}
