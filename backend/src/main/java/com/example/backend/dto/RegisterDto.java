package com.example.backend.dto;

/**
 * DTO для регистрации нового пользователя.
 *
 * Содержит email и пароль в незашифрованном виде (шифрование происходит в
 * AuthService).
 * Используется в
 * {@link com.example.backend.controller.AuthController#register()}.
 */
public class RegisterDto {

    private String email;
    private String password;

    /**
     * Конструктор по умолчанию (для Jackson при десериализации
     * multipart/form-data).
     */
    public RegisterDto() {
    }

    /**
     * Возвращает email пользователя для регистрации.
     *
     * @return email в формате example@mail.com или null.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Устанавливает email пользователя для регистрации.
     *
     * @param email уникальный email (валидация в AuthService).
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Возвращает пароль пользователя для регистрации.
     *
     * @return пароль в plain text (шифруется BCrypt в AuthService).
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль пользователя для регистрации.
     *
     * @param password пароль (минимум 6 символов, валидация в AuthService).
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
