package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * JPA-сущность для представления товара в каталоге интернет-магазина.
 *
 * Связан с категорией (многие-к-одному). Поле isCart используется для корзины.
 * Используется в {@link com.example.backend.controller.ProductController}.
 */
@Entity
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private String imageURL;
  private double price;
  private boolean isCart;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  @JsonIgnore
  private Category category;

  /**
   * Конструктор по умолчанию (требуется JPA/Hibernate).
   */
  public Product() {
  }

  /**
   * Конструктор для создания товара с полным заполнением.
   *
   * @param name        название товара (отображается в каталоге).
   * @param description подробное описание товара.
   * @param imageURL    путь к изображению ("/images/catalog/product-123.jpg").
   * @param price       цена товара (double).
   * @param isCart      флаг "в корзине пользователя" (false по умолчанию).
   * @param category    связанная категория.
   */
  public Product(String name, String description, String imageURL,
      double price, boolean isCart, Category category) {
    this.name = name;
    this.description = description;
    this.imageURL = imageURL;
    this.price = price;
    this.isCart = isCart;
    this.category = category;
  }

  /**
   * Возвращает уникальный идентификатор товара.
   *
   * @return автогенерируемый ID (primary key).
   */
  public Long getId() {
    return id;
  }

  /**
   * Устанавливает ID товара (используется JPA).
   *
   * @param id первичный ключ товара.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Возвращает название товара.
   *
   * @return человекочитаемое название (например, "iPhone 15 Pro").
   */
  public String getName() {
    return name;
  }

  /**
   * Устанавливает название товара.
   *
   * @param name новое название товара.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Возвращает описание товара.
   *
   * @return подробное описание характеристик.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Устанавливает описание товара.
   *
   * @param description текст описания.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Возвращает URL изображения товара.
   *
   * @return путь к изображению ("/images/catalog/product-123.jpg") или null.
   */
  public String getImageURL() {
    return imageURL;
  }

  /**
   * Устанавливает URL изображения товара.
   *
   * @param imageURL публичный путь к изображению.
   */
  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  /**
   * Возвращает цену товара.
   *
   * @return цена в основной валюте (например, RUB).
   */
  public double getPrice() {
    return price;
  }

  /**
   * Устанавливает цену товара.
   *
   * @param price новая цена (double).
   */
  public void setPrice(double price) {
    this.price = price;
  }

  /**
   * Возвращает флаг "товар в корзине".
   *
   * @return true если товар добавлен в корзину текущего пользователя.
   */
  public boolean getIsCart() {
    return isCart;
  }

  /**
   * Устанавливает флаг "в корзине".
   *
   * @param isCart true для добавления в корзину, false для каталога.
   */
  public void setIsCart(boolean isCart) {
    this.isCart = isCart;
  }

  /**
   * Возвращает связанную категорию товара.
   *
   * @return сущность {@link Category} (ленивая загрузка).
   */
  public Category getCategory() {
    return category;
  }

  /**
   * Устанавливает категорию для товара.
   *
   * @param category категория из БД.
   */
  public void setCategory(Category category) {
    this.category = category;
  }

  /**
   * Геттер для ID категории (для JSON-ответа).
   *
   * @JsonIgnore на getCategory() предотвращает циклические ссылки и ленивую
   *             инициализацию.
   *             Этот метод возвращает ID вместо полной сущности Category.
   *
   * @return ID категории или null.
   */
  public Long getCategoryId() {
    return category != null ? category.getId() : null;
  }
}
