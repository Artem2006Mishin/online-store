package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA-сущность для представления новости в системе.
 *
 * Состоит из уникального ID, заголовка, текста и изображения.
 * Используется в {@link com.example.backend.controller.NewsController} и
 * {@link com.example.backend.repository.NewsRepository}.
 */
@Entity
@Table(name = "news")
public class News {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String text;
  private String imageURL;

  /**
   * Конструктор по умолчанию (требуется JPA/Hibernate).
   */
  public News() {
  }

  /**
   * Конструктор для создания новости с полным заполнением.
   *
   * @param title    заголовок новости (отображается в UI).
   * @param text     содержимое новости (HTML или plain text).
   * @param imageURL путь к изображению ("/images/news/news-123.jpg").
   */
  public News(String title, String text, String imageURL) {
    this.title = title;
    this.text = text;
    this.imageURL = imageURL;
  }

  /**
   * Возвращает уникальный идентификатор новости.
   *
   * @return автогенерируемый ID (primary key).
   */
  public Long getId() {
    return id;
  }

  /**
   * Устанавливает ID новости (используется JPA).
   *
   * @param id первичный ключ новости.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Возвращает заголовок новости.
   *
   * @return краткое название новости.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Устанавливает заголовок новости.
   *
   * @param title новый заголовок.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Возвращает содержимое новости.
   *
   * @return полный текст новости.
   */
  public String getText() {
    return text;
  }

  /**
   * Устанавливает содержимое новости.
   *
   * @param text текст новости.
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Возвращает URL изображения новости.
   *
   * @return путь к изображению ("/images/news/news-123.jpg") или null.
   */
  public String getImageURL() {
    return imageURL;
  }

  /**
   * Устанавливает URL изображения новости.
   *
   * @param imageURL публичный путь к изображению.
   */
  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }
}
