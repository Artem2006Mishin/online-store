package com.example.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA-сущность для представления пользователя системы.
 *
 * Основная таблица аутентификации. Используется в
 * {@link com.example.backend.service.UserDetailServiceImpl}
 * для Spring Security и во всех контроллерах профилей/заказов.
 */
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String role = "USER"; // по умолчанию

  @Column(nullable = false)
  private int loginCount = 0;

  @Column
  private String avatarUrl; // может быть null

  /**
   * Конструктор по умолчанию (требуется JPA/Hibernate).
   */
  public User() {
  }

  /**
   * Конструктор для создания пользователя при регистрации.
   *
   * @param email    уникальный email (primary login).
   * @param password пароль в BCrypt-шифрованном виде (из AuthService).
   */
  public User(String email, String password) {
    this.email = email;
    this.password = password;
    this.role = "USER";
  }

  /**
   * Возвращает уникальный идентификатор пользователя.
   *
   * @return автогенерируемый ID (primary key).
   */
  public Long getId() {
    return id;
  }

  /**
   * Устанавливает ID пользователя (используется JPA).
   *
   * @param id первичный ключ пользователя.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Возвращает email пользователя (логин).
   *
   * @return уникальный email (username для Spring Security).
   */
  public String getEmail() {
    return email;
  }

  /**
   * Устанавливает email пользователя.
   *
   * @param email новый уникальный email.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Возвращает зашифрованный пароль.
   *
   * @return BCrypt-шифрованный пароль (не plain text).
   */
  public String getPassword() {
    return password;
  }

  /**
   * Устанавливает пароль пользователя.
   *
   * @param password BCrypt-шифрованный пароль (из PasswordEncoder).
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Возвращает роль пользователя.
   *
   * @return "USER", "MODERATOR", "ADMIN" (используется в
   *         SecurityConfig.hasRole()).
   */
  public String getRole() {
    return role;
  }

  /**
   * Устанавливает роль пользователя (админская операция).
   *
   * @param role новая роль ("USER", "MODERATOR", "ADMIN").
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * Возвращает количество входов в систему.
   *
   * @return счётчик логинов (инкрементируется в AuthService.login()).
   */
  public int getLoginCount() {
    return loginCount;
  }

  /**
   * Устанавливает счётчик логинов.
   *
   * @param loginCount новое значение (обычно +1 при login).
   */
  public void setLoginCount(int loginCount) {
    this.loginCount = loginCount;
  }

  /**
   * Возвращает URL аватара пользователя.
   *
   * @return путь к изображению ("/images/avatars/user-1.jpg") или null.
   */
  public String getAvatarUrl() {
    return avatarUrl;
  }

  /**
   * Устанавливает URL аватара.
   *
   * @param avatarUrl публичный путь к аватару или null.
   */
  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }
}
