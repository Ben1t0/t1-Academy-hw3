package ru.t1academy.java.hw3.repository;

import org.springframework.stereotype.Repository;
import ru.t1academy.java.hw3.model.RefreshToken;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class RefreshTokenRepository {

    private final ConcurrentMap<String, RefreshToken> refreshStorage = new ConcurrentHashMap<>();


    public void save(RefreshToken refreshToken) {
        refreshStorage.put(refreshToken.username(), refreshToken);
    }

    public RefreshToken find(String username) {
        if (refreshStorage.containsKey(username)) {
            var token = refreshStorage.get(username);
            if (token.expiresAt().before(new Date())) {
                refreshStorage.remove(username);
                return null;
            }
            return token;
        } else {
            return null;
        }
    }

    public void delete(String username) {
        refreshStorage.remove(username);
    }
}
