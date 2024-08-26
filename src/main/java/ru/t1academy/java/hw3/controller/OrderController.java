package ru.t1academy.java.hw3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.t1academy.java.hw3.dto.order.OrderDto;
import ru.t1academy.java.hw3.dto.order.ReturnOrderDto;
import ru.t1academy.java.hw3.service.order.OrderService;

import java.util.List;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "Authorization")
@Tag(name = "Orders API", description = "API for manipulate orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get order by ID")
    @ApiResponses({@ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ReturnOrderDto.class), mediaType = "application/json")})})
    @GetMapping("/api/v1/orders/{orderId}")
    public ReturnOrderDto getOrderById(@PathVariable long orderId) {
        return orderService.getById(orderId);
    }

    @Operation(summary = "Get all orders")
    @ApiResponses({@ApiResponse(responseCode = "200", content = {
            @Content(array =
            @ArraySchema(schema = @Schema(implementation = ReturnOrderDto.class)), mediaType = "application/json")})})
    @GetMapping("/api/v1/orders")
    public List<ReturnOrderDto> getAllOrders() {
        return orderService.getAll();
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/orders")
    public ReturnOrderDto createOrder(@RequestBody OrderDto orderDto) {
        return orderService.addOrder(orderDto);
    }

    @Operation(
            summary = "Update order",
            description = "Update order description and/or customer ID.")
    @ApiResponses({@ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ReturnOrderDto.class), mediaType = "application/json")})})
    @PutMapping("/api/v1/orders/{orderId}")
    public ReturnOrderDto updateOrder(@PathVariable long orderId, @RequestBody OrderDto orderDto) {
        return orderService.updateOrder(orderId, orderDto);
    }

    @Operation(
            summary = "Delete order",
            description = "Available for admins. Delete order by id.")
    @ApiResponses({@ApiResponse(responseCode = "200")})
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/v1/orders/{orderId}")
    public void deleteOrder(@PathVariable long orderId) {
        orderService.deleteOrder(orderId);
    }
}
