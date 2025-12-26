package com.example.backend.controller;

import com.example.backend.model.Cart;
import com.example.backend.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Cart myCart() {
        return cartService.getOrCreateActiveCart();
    }

    @PostMapping("/add")
    public Cart add(@RequestParam Long productId, @RequestParam(defaultValue = "1") int qty) {
        return cartService.addProduct(productId, qty);
    }
}
