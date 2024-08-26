package ru.t1academy.java.hw3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.t1academy.java.hw3.dto.user.ReturnUserDto;
import ru.t1academy.java.hw3.dto.user.UserDto;
import ru.t1academy.java.hw3.service.user.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Users API", description = "API for manipulate users")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Get user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ReturnUserDto.class),
                            mediaType = "application/json")})})
    @GetMapping("/api/v1/users/{userId}")
    public ReturnUserDto getUserById(@PathVariable long userId) {
        return userService.getById(userId);
    }

    @Operation(
            summary = "Get all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = ReturnUserDto.class)),
                            mediaType = "application/json")})})
    @GetMapping("/api/v1/users")
    public List<ReturnUserDto> getAllUsers() {
        return userService.getAll();
    }

    @Operation(
            summary = "Update user",
            description = "Available for admins. Update user email and/or roles and/or password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ReturnUserDto.class),
                            mediaType = "application/json")})})
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/v1/users/{userId}")
    public ReturnUserDto updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @Operation(
            summary = "Delete user",
            description = "Available for admins. Delete user by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200")})
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/v1/users/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
