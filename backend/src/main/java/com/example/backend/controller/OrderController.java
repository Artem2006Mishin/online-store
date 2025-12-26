package com.example.backend.controller;

import com.example.backend.dto.CreateOrderDto;
import com.example.backend.model.Order;
import com.example.backend.service.OrderService;
import org.springframework.web.bind.annotation.*;

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
}
