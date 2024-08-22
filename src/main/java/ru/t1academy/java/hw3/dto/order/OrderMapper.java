package ru.t1academy.java.hw3.dto.order;

import org.springframework.stereotype.Service;
import ru.t1academy.java.hw3.model.Order;

@Service
public class OrderMapper {
    public ReturnOrderDto toDto(Order order) {
        if (order == null) return null;
        return ReturnOrderDto.builder()
                .description(order.getDescription())
                .status(order.getStatus().name())
                .customerId(order.getCustomer().getId())
                .id(order.getId())
                .build();
    }
}
