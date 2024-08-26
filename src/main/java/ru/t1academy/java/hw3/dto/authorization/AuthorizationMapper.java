package ru.t1academy.java.hw3.dto.authorization;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.t1academy.java.hw3.model.UserDetailsImpl;

@Service
public class AuthorizationMapper {
    public JwtResponse toJwtResponse(UserDetailsImpl user, String accessToken, String refreshToken) {
        if (user != null) {
            return JwtResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                            .toList())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        return null;
    }
}