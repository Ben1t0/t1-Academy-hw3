package ru.t1academy.java.hw3.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.t1academy.java.hw3.dto.user.ReturnUserDto;
import ru.t1academy.java.hw3.dto.user.UserDto;
import ru.t1academy.java.hw3.service.user.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/v1/users/{userId}")
    public ReturnUserDto getUserById(@PathVariable long userId) {
        return userService.getById(userId);
    }

    @GetMapping("/api/v1/users")
    public List<ReturnUserDto> getAllUsers() {
        return userService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/users")
    public ReturnUserDto createUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PutMapping("/api/v1/users/{userId}")
    public ReturnUserDto updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/api/v1/users/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
