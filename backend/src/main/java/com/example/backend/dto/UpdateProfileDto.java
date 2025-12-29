package com.example.backend.dto;

/**
 * DTO для частичного обновления профиля пользователя.
 *
 * Содержит опциональные поля email и пароль (null = не обновлять).
 * Используется в
 * {@link com.example.backend.controller.ProfileController#updateProfile()}.
 */
public class UpdateProfileDto {

    private String email;
    private String password;

    /**
     * Конструктор по умолчанию (для Jackson при десериализации
     * multipart/form-data).
     */
    public UpdateProfileDto() {
    }

    /**
     * Возвращает новый email для обновления профиля.
     *
     * @return новый email или null (поле не обновляется).
     */
    public String getEmail() {
        return email;
    }

    /**
     * Устанавливает новый email для профиля.
     *
     * @param email новый email (может быть null для пропуска обновления).
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Возвращает новый пароль для обновления профиля.
     *
     * @return новый пароль в plain text или null (поле не обновляется).
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает новый пароль для профиля.
     *
     * @param password новый пароль (null = не менять, шифруется BCrypt в
     *                 AuthService).
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
