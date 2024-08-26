package ru.t1academy.java.hw3.service.authorization;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1academy.java.hw3.dto.authorization.AuthorizationMapper;
import ru.t1academy.java.hw3.dto.authorization.CreateUserDto;
import ru.t1academy.java.hw3.dto.authorization.JwtLoginDto;
import ru.t1academy.java.hw3.dto.authorization.JwtRefresh;
import ru.t1academy.java.hw3.dto.authorization.JwtResponse;
import ru.t1academy.java.hw3.dto.user.ReturnUserDto;
import ru.t1academy.java.hw3.dto.user.UserMapper;
import ru.t1academy.java.hw3.exception.AuthException;
import ru.t1academy.java.hw3.exception.ConflictException;
import ru.t1academy.java.hw3.model.RefreshToken;
import ru.t1academy.java.hw3.model.Role;
import ru.t1academy.java.hw3.model.User;
import ru.t1academy.java.hw3.model.UserDetailsImpl;
import ru.t1academy.java.hw3.repository.RefreshTokenRepository;
import ru.t1academy.java.hw3.repository.UserRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;

    private final AuthorizationMapper authorizationMapper;

    private final UserMapper userMapper;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final JwtProvider provider;

    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public JwtResponse login(JwtLoginDto jwtLoginDto) {

        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(jwtLoginDto.username(), jwtLoginDto.password()));

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("User is locked");
        }

        if (!userDetails.isEnabled()) {
            throw new DisabledException("User is disabled");
        }

        final String accessToken = provider.generateAccessToken(userDetails);
        final String refreshToken = provider.generateRefreshToken(userDetails);

        return authorizationMapper.toJwtResponse(userDetails, accessToken, refreshToken);
    }

    public JwtResponse getAccessToken(JwtRefresh jwtRefresh) {
        String refreshToken = jwtRefresh.refreshToken();

        if (provider.validateRefreshToken(refreshToken)) {
            final Claims claims = provider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final RefreshToken savedRefreshToken = refreshTokenRepository.find(username);
            if (savedRefreshToken != null && savedRefreshToken.token().equals(refreshToken)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                final String accessToken = provider.generateAccessToken(userDetails);

                return JwtResponse.builder().accessToken(accessToken).build();
            }
        }
        throw new AuthException("Invalid refresh token");
    }

    public JwtResponse refresh(JwtRefresh jwtRefresh) {
        String refreshToken = jwtRefresh.refreshToken();

        if (provider.validateRefreshToken(refreshToken)) {
            final Claims claims = provider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final RefreshToken savedRefreshToken = refreshTokenRepository.find(username);

            if (savedRefreshToken != null && savedRefreshToken.token().equals(refreshToken)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                final String accessToken = provider.generateAccessToken(userDetails);
                final String newRefreshToken = provider.generateRefreshToken(userDetails);

                return JwtResponse.builder().accessToken(accessToken).refreshToken(newRefreshToken).build();
            }
        }
        throw new AuthException("Invalid refresh token");
    }

    public void logout(UserDetails userDetails) {
        refreshTokenRepository.delete(userDetails.getUsername());
    }

    @Transactional
    public ReturnUserDto registerUser(CreateUserDto createUserDto) {
        if (userRepository.existsByEmail(createUserDto.email())) {
            throw new ConflictException("User with email %s already exists".formatted(createUserDto.email()));
        }
        if (userRepository.existsByUsername(createUserDto.username())) {
            throw new ConflictException("User with username %s already exists".formatted(createUserDto.username()));
        }

        User user = User.builder()
                .username(createUserDto.username())
                .email(createUserDto.email())
                .password(passwordEncoder.encode(createUserDto.password()))
                .roles(createUserDto.roles().stream()
                        .map(r -> Role.valueOf(r.toUpperCase()))
                        .collect(Collectors.toSet()))
                .build();

        return userMapper.toDto(userRepository.save(user));
    }
}
