package ru.t1academy.java.hw3.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.t1academy.java.hw3.exception.NotFoundException;
import ru.t1academy.java.hw3.model.UserDetailsImpl;
import ru.t1academy.java.hw3.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Using user details service for getting user");
        return UserDetailsImpl.build(userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User %s not found".formatted(username))));
    }
}
