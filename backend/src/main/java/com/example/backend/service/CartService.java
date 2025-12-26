package com.example.backend.service;

import com.example.backend.model.*;
import com.example.backend.repository.CartItemRepository;
import com.example.backend.repository.CartRepository;
import com.example.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            CurrentUserService currentUserService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public Cart getOrCreateActiveCart() {
        User user = currentUserService.getCurrentUser();
        return cartRepository.findByUserIdAndStatus(user.getId(), CartStatus.ACTIVE)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    @Transactional
    public Cart addProduct(Long productId, int qty) {
        Cart cart = getOrCreateActiveCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseGet(() -> cartItemRepository.save(new CartItem(cart, product, 0)));

        item.setQuantity(item.getQuantity() + Math.max(qty, 1));
        cartItemRepository.save(item);

        return cart;
    }

    @Transactional
    public void clearCart(Cart cart) {
        cart.getItems().clear(); // orphanRemoval удалит позиции
        cartRepository.save(cart);
    }
}
