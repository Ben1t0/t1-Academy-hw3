package ru.t1academy.java.hw3.service.order;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1academy.java.hw3.dto.order.OrderDto;
import ru.t1academy.java.hw3.dto.order.OrderMapper;
import ru.t1academy.java.hw3.dto.order.ReturnOrderDto;
import ru.t1academy.java.hw3.exception.NotFoundException;
import ru.t1academy.java.hw3.model.Order;
import ru.t1academy.java.hw3.model.OrderStatus;
import ru.t1academy.java.hw3.model.User;
import ru.t1academy.java.hw3.repository.OrderRepository;
import ru.t1academy.java.hw3.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ReturnOrderDto getById(long orderId) {
        return orderMapper.toDto(orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReturnOrderDto> getAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ReturnOrderDto addOrder(OrderDto orderDto) {
        User customer = userRepository.findById(orderDto.customerId())
                .orElseThrow(() -> new NotFoundException("User with id %d not found".formatted(orderDto.customerId())));

        Order order = Order.builder()
                .description(orderDto.description())
                .status(OrderStatus.NEW)
                .customer(customer)
                .build();

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public ReturnOrderDto updateOrder(long orderId, OrderDto orderDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with id %d not found".formatted(orderId)));

        if (orderDto.description() != null && !orderDto.description().isBlank()) {
            order.setDescription(orderDto.description());
        }

        if (orderDto.customerId() != null) {
            User customer = userRepository.findById(orderDto.customerId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            order.setCustomer(customer);
        }

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void deleteOrder(long orderId) {
        orderRepository.deleteById(orderId);
    }
}
