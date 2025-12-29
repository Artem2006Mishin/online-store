package com.example.backend.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * JPA-сущность для представления заказа пользователя.
 *
 * Связывает пользователя с его заказами (один-ко-многим).
 * Используется в {@link com.example.backend.controller.OrderController} и
 * {@link com.example.backend.service.OrderService}.
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private double totalPrice = 0.0;

    /**
     * Конструктор по умолчанию (требуется JPA/Hibernate).
     */
    public Order() {
    }

    /**
     * Возвращает уникальный идентификатор заказа.
     *
     * @return автогенерируемый ID (primary key).
     */
    public Long getId() {
        return id;
    }

    /**
     * Возвращает пользователя, создавшего заказ.
     *
     * @return связанная сущность {@link User} (ленивая загрузка).
     */
    public User getUser() {
        return user;
    }

    /**
     * Устанавливает пользователя для заказа.
     *
     * @param user авторизованный пользователь, создавший заказ.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Возвращает дату и время создания заказа (UTC).
     *
     * @return {@link Instant} момент создания заказа.
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Устанавливает время создания заказа.
     *
     * @param createdAt момент создания (UTC Instant).
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Возвращает общую стоимость заказа.
     *
     * @return сумма всех товаров в заказе (считается в OrderService).
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Устанавливает общую стоимость заказа.
     *
     * @param totalPrice итоговая цена (сумма цен товаров × количество).
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
