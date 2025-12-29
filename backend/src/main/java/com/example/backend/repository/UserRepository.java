package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.User;

/**
 * Spring Data JPA репозиторий для сущности {@link User}.
 *
 * Предоставляет поиск пользователя по email (основной логин).
 * Используется в {@link com.example.backend.service.UserDetailServiceImpl} и
 * контроллерах.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Находит пользователя по уникальному email.
   *
   * Генерирует SQL: `SELECT * FROM users WHERE email = ? LIMIT 1`.
   *
   * @param email уникальный email пользователя.
   * @return {@link Optional}&lt;{@link User}&gt; — пользователь или пустой
   *         Optional.
   */
  Optional<User> findByEmail(String email);
}
