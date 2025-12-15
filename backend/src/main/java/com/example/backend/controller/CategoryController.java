package com.example.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.example.backend.model.Category;
import com.example.backend.repository.CategoryRepository;

import java.util.List;

@RestController
@RequestMapping("categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {
  private final CategoryRepository repository;

  public CategoryController(CategoryRepository repository) {
    this.repository = repository;
  }
  
  @GetMapping("/getCategories")
  public List<Category> getAll() {
    return this.repository.findAll();
  }
}
