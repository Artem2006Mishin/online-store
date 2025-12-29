package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Category;

/**
 * Spring Data JPA репозиторий для сущности {@link Category}.
 *
 * Предоставляет стандартные CRUD-операции + методы поиска по ID.
 * Используется в {@link com.example.backend.controller.ProductController} и
 * {@link com.example.backend.controller.CategoryController}.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Наследуемые методы:
    // - findAll(), save(), deleteById(), findById(), existsById(), count()
}
