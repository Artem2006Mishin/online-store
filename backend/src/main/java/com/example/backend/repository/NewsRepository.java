package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.News;

/**
 * Spring Data JPA репозиторий для сущности {@link News}.
 *
 * Предоставляет стандартные CRUD-операции + сортировку.
 * Используется в {@link com.example.backend.controller.NewsController}.
 */
public interface NewsRepository extends JpaRepository<News, Long> {
    // Наследуемые методы:
    // - findAll(), findAll(Sort), save(), deleteById(), findById()
}
