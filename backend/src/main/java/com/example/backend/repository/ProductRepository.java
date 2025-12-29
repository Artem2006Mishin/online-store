package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Product;

/**
 * Spring Data JPA репозиторий для сущности {@link Product}.
 *
 * Предоставляет кастомные методы поиска по имени и фильтрацию по категории.
 * Используется в {@link com.example.backend.controller.ProductController}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  /**
   * Находит товар по точному названию.
   *
   * Генерирует SQL: `SELECT * FROM products WHERE name = ?`.
   *
   * @param name точное название товара.
   * @return {@link Optional}&lt;{@link Product}&gt; — товар или пустой Optional.
   */
  Optional<Product> findByName(String name);

  /**
   * Находит все товары определённой категории по названию категории.
   *
   * Генерирует SQL: `SELECT * FROM products p JOIN category c ON p.category_id =
   * c.id WHERE c.title = ?`.
   *
   * @param title название категории (например, "Electronics").
   * @return {@link List}&lt;{@link Product}&gt; — товары категории.
   *         Пустой список, если категория не существует.
   */
  List<Product> findByCategory_Title(String title); // ← ФИЛЬТР ПО КАТЕГОРИИ
}
