package ru.t1academy.java.hw3.dto.user;

import lombok.Builder;

import java.util.List;

@Builder
public record ReturnUserDto(long id, String username, String email, List<String> roles) {
}
