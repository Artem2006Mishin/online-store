package com.example.backend.model;

import jakarta.persistence.*;

/*
 * @Entity - помечает класс как сущность связанную с таблицей в БД
 * @Table(name = "news") - в БД будет таблица с именем news
 * @Id - задает первичный ключ для таблицы (в таблице news будет несколько 
 * новостей, и для каждой новости будет уникальный идентификатор)
 * @GeneratedValue - автоматически генерирует ключи, когда добавляются данные 
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

  public News() {};
  public News(String title, String text, String imageURL) {
    this.title = title;
    this.text = text;
    this.imageURL = imageURL;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }

  public String getImageURL() {
    return imageURL;
  }
  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }
}

// TODO: добавить дату создания новости
