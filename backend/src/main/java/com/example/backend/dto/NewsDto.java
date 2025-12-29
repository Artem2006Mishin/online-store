package com.example.backend.dto;

/**
 * DTO для передачи данных о новости между слоями приложения.
 *
 * Содержит основные поля новости: заголовок, текст и URL изображения.
 * Используется для сериализации/десериализации JSON в контроллерах.
 */
public class NewsDto {

    private String title;
    private String text;
    private String imageURL;

    /**
     * Конструктор по умолчанию (для Jackson при десериализации JSON).
     */
    public NewsDto() {
    }

    /**
     * Конструктор с полным заполнением полей.
     *
     * @param title    заголовок новости (не null, используется в UI).
     * @param text     содержимое новости (не null).
     * @param imageURL URL изображения новости (может быть null).
     */
    public NewsDto(String title, String text, String imageURL) {
        this.title = title;
        this.text = text;
        this.imageURL = imageURL;
    }

    /**
     * Возвращает заголовок новости.
     *
     * @return заголовок новости или null.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Устанавливает заголовок новости.
     *
     * @param title заголовок новости.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Возвращает содержимое новости.
     *
     * @return текст новости или null.
     */
    public String getText() {
        return text;
    }

    /**
     * Устанавливает содержимое новости.
     *
     * @param text текст новости.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Возвращает URL изображения новости.
     *
     * @return путь к изображению (например, "/images/news/news-123.jpg") или null.
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Устанавливает URL изображения новости.
     *
     * @param imageURL публичный URL изображения.
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
