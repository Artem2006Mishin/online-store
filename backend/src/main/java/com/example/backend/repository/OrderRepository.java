package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Order;
import com.example.backend.model.User;

/**
 * Spring Data JPA репозиторий для сущности {@link Order}.
 *
 * Предоставляет стандартные CRUD + кастомный поиск заказов пользователя по
 * убыванию даты.
 * Используется в
 * {@link com.example.backend.service.OrderService#getUserOrders()}.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Находит все заказы указанного пользователя, отсортированные по дате создания
     * (новые сверху).
     *
     * Генерирует SQL: `SELECT * FROM orders WHERE user_id = ? ORDER BY createdAt
     * DESC`.
     *
     * @param user пользователь, чьи заказы нужно найти.
     * @return {@link List}&lt;{@link Order}&gt; — заказы пользователя (новые →
     *         старые).
     *         Пустой список, если заказов нет.
     */
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
