package com.example.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.model.Product;
import com.example.backend.repository.ProductRepository;
import com.example.backend.service.ProductService;

@RestController
@RequestMapping("/products") // убрал лишний /products в пути
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
  private final ProductRepository repository;
  @Autowired
  private ProductService productService;

  public ProductController(ProductRepository repository) {
    this.repository = repository;
  }

  @PostMapping("/by-name") // изменил путь
  public ResponseEntity<Product> getProductByName(@RequestBody String productName) {
    Long productId = productService.getProductIdByName(productName);
    Product product = repository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Товар не найден"));
    return ResponseEntity.ok(product);
  }

  @GetMapping("/category/{categoryName}")
  public List<Product> getProductsByCategory(@PathVariable String categoryName) {
    return repository.findByCategory_Title(categoryName); // ← ТОЛЬКО нужные товары
  }

}
