package com.example.backend.dto;

public class UserDto {
  private String email;
  private String password;

  public UserDto() {};

  public void setEmail(String email) {
    this.email = email;
  }
  public String getEmail() {
    return email;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  public String getPassword() {
    return password;
  }
}
