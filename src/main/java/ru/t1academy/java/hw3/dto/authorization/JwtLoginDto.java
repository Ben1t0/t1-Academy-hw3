package ru.t1academy.java.hw3.dto.authorization;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record JwtLoginDto(@NotBlank String username, @NotBlank String password) {
}
