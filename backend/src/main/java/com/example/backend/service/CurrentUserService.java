package com.example.backend.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

/**
 * Сервис для работы с текущим авторизованным пользователем.
 *
 * Извлекает пользователя из SecurityContext (JWT) и предоставляет удобный
 * доступ.
 * Используется в {@link com.example.backend.controller.ProfileController} и
 * аватар-эндпоинтах.
 */
@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    /**
     * Конструктор внедряет репозиторий пользователей.
     *
     * @param userRepository репозиторий для загрузки User по email из
     *                       SecurityContext.
     */
    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Возвращает текущего авторизованного пользователя из SecurityContext.
     *
     * 1. Извлекает email из JWT
     * (SecurityContextHolder.getAuthentication().getName()).
     * 2. Загружает полную сущность User из БД.
     *
     * @return {@link User} текущего пользователя.
     * @throws RuntimeException если пользователь не найден по email из токена.
     */
    public User getCurrentUser() {
        // email из JWT-токена (устанавливается JwtAuthFilter).
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    /**
     * Сохраняет изменения текущего пользователя в БД.
     *
     * Удобный метод для обновления avatarUrl, loginCount и других полей.
     *
     * @param user пользователь с изменениями (обычно из getCurrentUser()).
     * @return сохранённая сущность {@link User}.
     */
    public User save(User user) {
        return userRepository.save(user);
    }
}
