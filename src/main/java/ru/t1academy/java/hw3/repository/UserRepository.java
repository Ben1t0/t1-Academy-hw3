package ru.t1academy.java.hw3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1academy.java.hw3.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
