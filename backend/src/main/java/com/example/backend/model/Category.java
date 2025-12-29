package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA-сущность для представления категории товаров в каталоге.
 *
 * Состоит из ID, названия и изображения категории.
 * Используется в {@link com.example.backend.controller.ProductController} и
 * {@link com.example.backend.repository.CategoryRepository}.
 */
@Entity
@Table(name = "category")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String imageURL;

  /**
   * Конструктор по умолчанию (требуется JPA/Hibernate).
   */
  public Category() {
  }

  /**
   * Конструктор для создания категории с названием и изображением.
   *
   * @param title    название категории (например, "Электроника", "Одежда").
   * @param imageURL путь к изображению категории
   *                 ("/images/categories/electronics.jpg").
   */
  public Category(String title, String imageURL) {
    this.title = title;
    this.imageURL = imageURL;
  }

  /**
   * Возвращает уникальный идентификатор категории.
   *
   * @return автогенерируемый ID (Long, primary key).
   */
  public Long getId() {
    return id;
  }

  /**
   * Устанавливает ID категории (только для JPA/Hibernate).
   *
   * @param id первичный ключ категории.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Возвращает название категории.
   *
   * @return человекочитаемое название (например, "Смартфоны").
   */
  public String getTitle() {
    return title;
  }

  /**
   * Устанавливает название категории.
   *
   * @param title новое название категории.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Возвращает URL изображения категории.
   *
   * @return путь к изображению ("/images/categories/smartphones.jpg") или null.
   */
  public String getImageURL() {
    return imageURL;
  }

  /**
   * Устанавливает URL изображения категории.
   *
   * @param imageURL публичный путь к изображению категории.
   */
  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }
}
