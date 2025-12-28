package com.example.backend.service;

import com.example.backend.dto.CreateOrderDto;
import com.example.backend.model.Order;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    public OrderService(OrderRepository orderRepository,
            ProductRepository productRepository,
            CurrentUserService currentUserService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public Order createOrder(CreateOrderDto dto) {
        User user = currentUserService.getCurrentUser();

        List<Long> ids = dto.getProductIds();
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("Product list is empty");
        }

        List<Product> products = productRepository.findAllById(ids);

        if (products.size() != ids.size()) {
            throw new RuntimeException("Some products not found");
        }

        double total = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(Instant.now());
        order.setTotalPrice(total);

        return orderRepository.save(order);
    }

    public List<Order> getUserOrders() {
        User user = currentUserService.getCurrentUser();
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }
}
