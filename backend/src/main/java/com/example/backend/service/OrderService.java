package com.example.backend.service;

import com.example.backend.model.*;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(CartService cartService, OrderRepository orderRepository, CartRepository cartRepository) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Order checkout() {
        Cart cart = cartService.getOrCreateActiveCart();

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(cart.getUser());

        for (CartItem ci : cart.getItems()) {
            order.getItems().add(new OrderItem(
                    order,
                    ci.getProduct(),
                    ci.getQuantity(),
                    ci.getProduct().getPrice()));
        }

        Order saved = orderRepository.save(order);

        // закрываем корзину и создаём новую при следующем обращении
        cart.setStatus(CartStatus.ORDERED);
        cartRepository.save(cart);

        return saved;
    }
}
