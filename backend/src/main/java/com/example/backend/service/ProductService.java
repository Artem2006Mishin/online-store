package com.example.backend.service;

import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Long getProductIdByName(String productName) {
        Product product = productRepository.findByName(productName)
                .orElseThrow(() -> new RuntimeException("Товар '" + productName + "' не найден"));
        return product.getId();
    }
}
