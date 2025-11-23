package com.example.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String imageURL;

  public Category() {}
  public Category(String title, String imageURL) {
    this.title = title;
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

  public String getImageURL() { 
    return imageURL;
  }
  public void setImageURL(String imageURL) { 
    this.imageURL = imageURL;
  }
}
