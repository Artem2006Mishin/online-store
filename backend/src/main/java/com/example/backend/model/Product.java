package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

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

  public Product() {}
  public Product(String name, String description, String imageURL,
    double price, boolean isCart, Category category) {
      this.name = name;
      this.description = description;
      this.imageURL = imageURL;
      this.price = price;
      this.isCart = isCart;
      this.category = category;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public String getImageURL() {
    return imageURL;
  }
  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  public double getPrice() {
    return price;
  }
  public void setPrice(double price) {
    this.price = price;
  }

  public boolean getIsCart() {
    return isCart;
  }
  public void setIsCart(boolean isCart) {
    this.isCart = isCart;
  }

  public Category getCategory() {
    return category;
  }
  public void setCategory(Category category) {
    this.category = category;
  }
}

