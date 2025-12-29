package com.example.backend.controller;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.backend.model.Product;
import com.example.backend.model.Category;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.service.ProductService;

@RestController
@RequestMapping("/products") // убрал лишний /products в пути
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
  private final ProductRepository repository;
  @Autowired
  private ProductService productService;
  @Autowired
  private CategoryRepository categoryRepository;

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

  @GetMapping
  public List<Product> getAllProducts() {
    return repository.findAll();
  }

  @GetMapping("/category/{categoryName}")
  public List<Product> getProductsByCategory(@PathVariable String categoryName) {
    return repository.findByCategory_Title(categoryName); // ← ТОЛЬКО нужные товары
  }

  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Product createProduct(
      @RequestParam("name") String name,
      @RequestParam("price") String priceStr,
      @RequestParam(value = "image", required = false) MultipartFile image,
      @RequestParam("categoryId") String categoryIdStr) throws IOException {
    Product product = new Product();
    product.setName(name);

    try {
      double price = Double.parseDouble(priceStr);
      product.setPrice(price);
    } catch (NumberFormatException e) {
      throw new RuntimeException("Неверный формат цены: " + priceStr);
    }

    try {
      Long categoryId = Long.parseLong(categoryIdStr);
      Category category = categoryRepository.findById(categoryId)
          .orElseThrow(() -> new RuntimeException("Категория не найдена"));
      product.setCategory(category);
    } catch (NumberFormatException e) {
      throw new RuntimeException("Неверный формат ID категории: " + categoryIdStr);
    }

    if (image != null && !image.isEmpty()) {
      Path uploadDir = Paths.get("src/main/resources/static/images/catalog");
      Files.createDirectories(uploadDir);

      String originalName = StringUtils.cleanPath(image.getOriginalFilename());
      String filename = "product-" + System.currentTimeMillis() + "-" + originalName;

      Path target = uploadDir.resolve(filename);
      Files.copy(image.getInputStream(), target);

      String url = "/images/catalog/" + filename;
      product.setImageURL(url);
    } else {
      product.setImageURL(null); // no image
    }

    product.setIsCart(false);
    return repository.save(product);
  }

  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Product updateProduct(
      @PathVariable Long id,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "price", required = false) String priceStr,
      @RequestParam(value = "image", required = false) MultipartFile image,
      @RequestParam(value = "categoryId", required = false) String categoryIdStr) throws IOException {
    Product product = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Товар не найден"));

    if (name != null && !name.isEmpty()) {
      product.setName(name);
    }

    if (priceStr != null && !priceStr.isEmpty()) {
      try {
        double price = Double.parseDouble(priceStr);
        product.setPrice(price);
      } catch (NumberFormatException e) {
        throw new RuntimeException("Неверный формат цены: " + priceStr);
      }
    }

    if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
      try {
        Long categoryId = Long.parseLong(categoryIdStr);
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        product.setCategory(category);
      } catch (NumberFormatException e) {
        throw new RuntimeException("Неверный формат ID категории: " + categoryIdStr);
      }
    }

    if (image != null && !image.isEmpty()) {
      Path uploadDir = Paths.get("src/main/resources/static/images/catalog");
      Files.createDirectories(uploadDir);

      String originalName = StringUtils.cleanPath(image.getOriginalFilename());
      String filename = "product-" + System.currentTimeMillis() + "-" + originalName;

      Path target = uploadDir.resolve(filename);
      Files.copy(image.getInputStream(), target);

      String url = "/images/catalog/" + filename;
      product.setImageURL(url);
    }

    return repository.save(product);
  }

  @DeleteMapping("/{id}")
  public void deleteProduct(@PathVariable Long id) {
    repository.deleteById(id);
  }

}
