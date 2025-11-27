package com.example.backend.dto;

public class UserResponseDto {
  private String email;
  private String token;

  public UserResponseDto(String email, String token) {
    this.email = email;
    this.token = token;
  };

  public String getEmail() {
    return email;
  }
  public String getToken() {
    return token;
  }
}
