package com.example.backend.dto;

/**
 * DTO для ответа с данными аутентифицированного пользователя.
 *
 * Возвращается после успешного логина/регистрации/обновления профиля.
 * Используется в {@link com.example.backend.controller.AuthController} и
 * {@link com.example.backend.controller.ProfileController}.
 */
public class UserResponseDto {

  private final String email;
  private final String token;
  private final String role;
  private final int loginCount;
  private final String avatarUrl;

  /**
   * Конструктор с полным заполнением всех полей ответа.
   *
   * @param email      email пользователя.
   * @param token      JWT-токен для последующих запросов (Bearer token).
   * @param role       роль пользователя ("USER", "MODERATOR", "ADMIN").
   * @param loginCount количество входов в систему.
   * @param avatarUrl  публичный URL аватара ("/images/avatars/user-1.jpg" или
   *                   null).
   */
  public UserResponseDto(String email, String token, String role, int loginCount, String avatarUrl) {
    this.email = email;
    this.token = token;
    this.role = role;
    this.loginCount = loginCount;
    this.avatarUrl = avatarUrl;
  }

  /**
   * Возвращает email пользователя.
   *
   * @return email в формате example@mail.com.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Возвращает JWT-токен для авторизации.
   *
   * @return Bearer токен (например, "eyJhbGciOiJIUzI1NiIs...") или null.
   */
  public String getToken() {
    return token;
  }

  /**
   * Возвращает роль пользователя.
   *
   * @return "USER", "MODERATOR", "ADMIN".
   */
  public String getRole() {
    return role;
  }

  /**
   * Возвращает количество входов пользователя в систему.
   *
   * @return целое число (инкрементируется при каждом login).
   */
  public int getLoginCount() {
    return loginCount;
  }

  /**
   * Возвращает URL аватара пользователя.
   *
   * @return путь к изображению ("/images/avatars/user-1.jpg") или null.
   */
  public String getAvatarUrl() {
    return avatarUrl;
  }
}
