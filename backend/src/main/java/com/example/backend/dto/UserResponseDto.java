package com.example.backend.dto;

public class UserResponseDto {
  private String email;
  private String token;
  private String role;
  private int loginCount;
  private String avatarUrl;

  public UserResponseDto(String email, String token, String role, int loginCount, String avatarUrl) {
    this.email = email;
    this.token = token;
    this.role = role;
    this.loginCount = loginCount;
    this.avatarUrl = avatarUrl;
  }

  public String getEmail() {
    return email;
  }
  
  public String getToken() {
    return token;
  }
  
  public String getRole() {
    return role;
  }
  
  public int getLoginCount() {
    return loginCount;
  }
  
  public String getAvatarUrl() {
    return avatarUrl;
  }
}
