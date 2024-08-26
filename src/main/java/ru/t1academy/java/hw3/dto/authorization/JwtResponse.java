package ru.t1academy.java.hw3.dto.authorization;

import lombok.Builder;

import java.util.List;

@Builder
public record JwtResponse(
        Long id,
        String username,
        List<String> roles,
        String accessToken,
        String refreshToken) {
}
