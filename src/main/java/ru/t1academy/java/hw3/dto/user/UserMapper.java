package ru.t1academy.java.hw3.dto.user;

import org.springframework.stereotype.Service;
import ru.t1academy.java.hw3.model.User;

@Service
public class UserMapper {
    public ReturnUserDto toDto(User user) {
        if (user == null) return null;
        return ReturnUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public User fromDto(UserDto userDto) {
        if (userDto == null) return null;
        return User.builder()
                .email(userDto.email())
                .name(userDto.name())
                .build();
    }
}
