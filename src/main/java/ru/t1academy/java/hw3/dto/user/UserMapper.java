package ru.t1academy.java.hw3.dto.user;

import org.springframework.stereotype.Service;
import ru.t1academy.java.hw3.model.Role;
import ru.t1academy.java.hw3.model.User;

@Service
public class UserMapper {
    public ReturnUserDto toDto(User user) {
        if (user == null) return null;
        return ReturnUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .roles(user.getRoles().stream()
                        .map(Role::name)
                        .toList())
                .build();
    }
}
