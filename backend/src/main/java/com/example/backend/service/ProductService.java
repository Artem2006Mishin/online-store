package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;

/**
 * Сервис для поиска товаров по названию.
 *
 * Используется в
 * {@link com.example.backend.controller.ProductController#getProductByName()}.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Находит ID товара по точному названию.
     *
     * @param productName точное название товара (например, "iPhone 15 Pro").
     * @return ID товара (Long).
     * @throws RuntimeException если товар с таким названием не найден.
     */
    public Long getProductIdByName(String productName) {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new RuntimeException("Товар '" + productName + "' не найден"));
        return product.getId();
    }
}
