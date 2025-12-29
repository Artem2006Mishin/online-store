package com.example.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.model.Category;
import com.example.backend.repository.CategoryRepository;

/**
 * REST-контроллер для управления категориями товаров.
 *
 * Предоставляет только чтение (GET) списка всех категорий.
 * Использует Spring Data JPA репозиторий для доступа к БД.
 */
@RestController
@RequestMapping("categories")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

  private final CategoryRepository repository;

  /**
   * Конструктор внедряет репозиторий категорий для доступа к данным.
   *
   * @param repository Spring Data JPA репозиторий для сущности {@link Category}.
   */
  public CategoryController(CategoryRepository repository) {
    this.repository = repository;
  }

  /**
   * Возвращает список всех категорий из базы данных.
   *
   * Эндпоинт доступен всем пользователям (публичный).
   *
   * @return {@link List}&lt;{@link Category}&gt; — полный список всех категорий.
   *         Пустой список, если таблица пуста.
   */
  @GetMapping("/getCategories")
  public List<Category> getAll() {
    // Возвращаем все категории напрямую из репозитория.
    // Spring Data JPA метод findAll() выполняет SELECT * FROM categories.
    return this.repository.findAll();
  }
}
