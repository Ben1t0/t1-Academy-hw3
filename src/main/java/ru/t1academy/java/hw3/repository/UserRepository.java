package ru.t1academy.java.hw3.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1academy.java.hw3.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);

    @Override
    @EntityGraph(attributePaths = "roles")
    List<User> findAll();

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
