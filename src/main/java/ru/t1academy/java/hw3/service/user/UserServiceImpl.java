package ru.t1academy.java.hw3.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1academy.java.hw3.dto.user.ReturnUserDto;
import ru.t1academy.java.hw3.dto.user.UserDto;
import ru.t1academy.java.hw3.dto.user.UserMapper;
import ru.t1academy.java.hw3.exception.NotFoundException;
import ru.t1academy.java.hw3.model.Role;
import ru.t1academy.java.hw3.model.User;
import ru.t1academy.java.hw3.repository.RefreshTokenRepository;
import ru.t1academy.java.hw3.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    @Transactional(readOnly = true)
    public ReturnUserDto getById(long userId) {
        return userMapper.toDto(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId))));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReturnUserDto> getAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }


    @Override
    @Transactional
    public ReturnUserDto updateUser(long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));

        if (userDto.email() != null && !userDto.email().isBlank()) {
            user.setEmail(userDto.email());
        }
        if (userDto.password() != null && !userDto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDto.password()));
            refreshTokenRepository.delete(user.getUsername());
        }

        if (userDto.roles() != null && userDto.roles().size() > 0) {
            user.getRoles().clear();
            user.getRoles().addAll(userDto.roles().stream().map(Role::valueOf).collect(Collectors.toSet()));
        }

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}
