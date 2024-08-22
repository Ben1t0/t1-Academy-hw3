package ru.t1academy.java.hw3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1academy.java.hw3.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
