package ru.t1academy.java.hw3.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1academy.java.hw3.dto.user.ReturnUserDto;
import ru.t1academy.java.hw3.dto.user.UserDto;
import ru.t1academy.java.hw3.dto.user.UserMapper;
import ru.t1academy.java.hw3.exception.NotFoundException;
import ru.t1academy.java.hw3.model.User;
import ru.t1academy.java.hw3.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

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
    public ReturnUserDto addUser(UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public ReturnUserDto updateUser(long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(userId)));

        if (userDto.email() != null && !userDto.email().isBlank()) {
            user.setEmail(userDto.email());
        }
        if (userDto.name() != null && !userDto.name().isBlank()) {
            user.setName(user.getName());
        }

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}
