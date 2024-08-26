package ru.t1academy.java.hw3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.t1academy.java.hw3.dto.authorization.CreateUserDto;
import ru.t1academy.java.hw3.dto.authorization.JwtLoginDto;
import ru.t1academy.java.hw3.dto.authorization.JwtRefresh;
import ru.t1academy.java.hw3.dto.authorization.JwtResponse;
import ru.t1academy.java.hw3.dto.user.ReturnUserDto;
import ru.t1academy.java.hw3.service.authorization.AuthService;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication API", description = "API for authenticate users and obtain JWT tokens")
public class AuthorizationController {
    private final AuthService authService;

    @Operation(
            summary = "Login exists user",
            description = "Authenticate user and return access and refresh tokens")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = JwtResponse.class),
                            mediaType = "application/json")})})
    @PostMapping("/api/v1/auth/login")
    public JwtResponse authenticateUser(@Valid @RequestBody JwtLoginDto jwtLoginDto) {
        return authService.login(jwtLoginDto);
    }

    @Operation(
            summary = "Get new access token",
            description = "Generate new access token if refresh token is valid")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = JwtResponse.class),
                            mediaType = "application/json")})})
    @PostMapping("/api/v1/auth/token")
    public JwtResponse getAccessToken(@Valid @RequestBody JwtRefresh jwtRefresh) {
        return authService.getAccessToken(jwtRefresh);
    }

    @Operation(
            summary = "Register new user in application")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = ReturnUserDto.class),
                            mediaType = "application/json")})})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/auth/register")
    public ReturnUserDto registerUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return authService.registerUser(createUserDto);
    }

    @Operation(
            summary = "Get new refresh token",
            description = "Generate new refresh token if old refresh token is valid")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = JwtResponse.class),
                            mediaType = "application/json")})})
    @PostMapping("/api/v1/auth/refresh")
    public JwtResponse refreshToken(@Valid @RequestBody JwtRefresh jwtRefresh) {
        return authService.refresh(jwtRefresh);
    }

    @Operation(
            summary = "Logout",
            description = "Delete refresh token from DB")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ReturnUserDto.class),
                            mediaType = "application/json")})})
    @PostMapping("/api/v1/auth/logout")
    public void logout(Authentication authentication) {
        if (authentication != null) {
            var requester = (UserDetails) authentication.getPrincipal();
            authService.logout(requester);
        }
    }
}
