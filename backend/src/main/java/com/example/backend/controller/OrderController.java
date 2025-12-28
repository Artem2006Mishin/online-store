package com.example.backend.controller;

import com.example.backend.dto.CreateOrderDto;
import com.example.backend.model.Order;
import com.example.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderDto dto) {
        return orderService.createOrder(dto);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders() {
        try {
            List<Order> orders = orderService.getUserOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}
