package com.example.backend.controller;

import com.example.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired
    private ProductService productService;

    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestBody List<String> productNames, Authentication auth) {
        List<Long> productIds = productNames.stream()
                .map(productService::getProductIdByName)
                .collect(Collectors.toList());
        // Логика создания заказа с productIds
        System.out.println("Заказ от " + auth.getName() + ": " + productIds);
        return ResponseEntity.ok("Заказ создан");
    }
}
