package com.example.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;

@RestController
@RequestMapping("products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
  private final ProductRepository repository;

  public ProductController(ProductRepository repository) {
    this.repository = repository;
  }
  
  @GetMapping("/getProducts/{id}")
  public List<Product> getProductByCategory(@PathVariable Long id) {
    return repository.findByCategoryId(id);
  }
}
