package ru.t1academy.java.hw3.dto.authorization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record CreateUserDto(@NotBlank String username, @Email @NotBlank String email, @NotBlank String password,
                            @NotEmpty Set<String> roles) {
}
