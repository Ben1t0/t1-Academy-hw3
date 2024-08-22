package ru.t1academy.java.hw3.dto.user;

import lombok.Builder;

@Builder
public record ReturnUserDto(long id, String name, String email) {
}
