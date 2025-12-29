package com.example.backend.dto;

/**
 * DTO для аутентификации пользователя (логин).
 *
 * Содержит email и пароль в незашифрованном виде.
 * Используется в {@link com.example.backend.controller.AuthController#login()}.
 */
public class UserDto {

  private String email;
  private String password;

  /**
   * Конструктор по умолчанию (для Jackson при десериализации JSON).
   */
  public UserDto() {
  }

  /**
   * Возвращает email пользователя для аутентификации.
   *
   * @return email в формате example@mail.com или null.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Устанавливает email пользователя для логина.
   *
   * @param email email пользователя (валидация в AuthService).
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Возвращает пароль пользователя для аутентификации.
   *
   * @return пароль в plain text (сравнивается с BCrypt в AuthService).
   */
  public String getPassword() {
    return password;
  }

  /**
   * Устанавливает пароль пользователя для логина.
   *
   * @param password пароль пользователя (BCrypt сравнение в
   *                 DaoAuthenticationProvider).
   */
  public void setPassword(String password) {
    this.password = password;
  }
}
