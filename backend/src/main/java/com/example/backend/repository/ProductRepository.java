package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByCategoryId(Long categoryId);
};
