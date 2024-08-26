package ru.t1academy.java.hw3.model;

import java.util.Date;

public record RefreshToken(String username, String token, Date expiresAt) {
}
