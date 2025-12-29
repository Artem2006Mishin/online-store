package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.CreateOrderDto;
import com.example.backend.model.Order;
import com.example.backend.service.OrderService;

/**
 * REST-контроллер для управления заказами авторизованных пользователей.
 *
 * Поддерживает:
 * - Создание нового заказа (требует авторизации).
 * - Получение списка заказов текущего пользователя.
 *
 * Использует {@link OrderService} для бизнес-логики.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Конструктор внедряет сервис заказов для обработки бизнес-логики.
     *
     * @param orderService сервис, инкапсулирующий создание и получение заказов
     *                     пользователя.
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Создает новый заказ для текущего авторизованного пользователя.
     *
     * Ожидает JSON с данными заказа ({@link CreateOrderDto}).
     * Требует авторизации (по SecurityConfig: anyRequest().authenticated()).
     *
     * @param dto данные для создания заказа (список товаров, адреса доставки и
     *            т.п.).
     * @return сохранённая сущность {@link Order} с автогенерированным ID.
     */
    @PostMapping
    public Order createOrder(@RequestBody CreateOrderDto dto) {
        // Передаем DTO в сервис для валидации, сохранения и генерации заказа.
        return orderService.createOrder(dto);
    }

    /**
     * Возвращает список всех заказов текущего авторизованного пользователя.
     *
     * Определяет пользователя через SecurityContext (JWT).
     * Требует авторизации (по SecurityConfig: anyRequest().authenticated()).
     *
     * @return {@link ResponseEntity}:
     *         <ul>
     *         <li>200 OK с {@link List}&lt;{@link Order}&gt; при успешном получении
     *         заказов.</li>
     *         <li>401 Unauthorized (пустое тело) при ошибках (нет пользователя,
     *         проблемы с БД).</li>
     *         </ul>
     */
    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders() {
        try {
            // Получаем заказы текущего пользователя через сервис.
            List<Order> orders = orderService.getUserOrders();
            // Успешный ответ с данными заказов.
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            // Любая ошибка (нет пользователя, проблемы БД) → 401 Unauthorized.
            // Пустое тело ответа без деталей ошибки.
            return ResponseEntity.status(401).build();
        }
    }
}
