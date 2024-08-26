package ru.t1academy.java.hw3.dto.user;

import lombok.Builder;

import java.util.List;

@Builder
public record UserDto(String username, String email, String password, List<String> roles) {
}
