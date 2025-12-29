package com.example.backend.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.CreateOrderDto;
import com.example.backend.model.Order;
import com.example.backend.model.Product;
import com.example.backend.model.User;
import com.example.backend.repository.OrderRepository;
import com.example.backend.repository.ProductRepository;

import jakarta.transaction.Transactional;

/**
 * Сервис для создания и получения заказов авторизованных пользователей.
 *
 * Создаёт заказы на основе списка ID товаров, вычисляет итоговую сумму.
 * Используется в {@link com.example.backend.controller.OrderController}.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    /**
     * Конструктор внедряет репозитории и сервис текущего пользователя.
     *
     * @param orderRepository    репозиторий заказов.
     * @param productRepository  репозиторий товаров.
     * @param currentUserService сервис для получения текущего пользователя из JWT.
     */
    public OrderService(OrderRepository orderRepository,
            ProductRepository productRepository,
            CurrentUserService currentUserService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.currentUserService = currentUserService;
    }

    /**
     * Создаёт новый заказ для текущего пользователя.
     *
     * 1. Получает текущего пользователя из SecurityContext.
     * 2. Загружает товары по ID.
     * 3. Вычисляет итоговую сумму.
     * 4. Сохраняет заказ в БД.
     *
     * @param dto список ID товаров для заказа ({@link CreateOrderDto}).
     * @return сохранённый {@link Order} с автогенерированным ID.
     * @throws RuntimeException если список товаров пустой или товары не найдены.
     */
    @Transactional
    public Order createOrder(CreateOrderDto dto) {
        // Получаем текущего авторизованного пользователя.
        User user = currentUserService.getCurrentUser();

        // Валидация входных данных.
        List<Long> ids = dto.getProductIds();
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("Product list is empty");
        }

        // Загружаем товары батчем (одним SQL-запросом).
        List<Product> products = productRepository.findAllById(ids);

        // Проверяем, что все товары существуют.
        if (products.size() != ids.size()) {
            throw new RuntimeException("Some products not found");
        }

        // Вычисляем итоговую сумму (параллельный stream для оптимизации).
        double total = products.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        // Создаём заказ.
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(Instant.now());
        order.setTotalPrice(total);

        // Сохраняем в БД (атомарная транзакция).
        return orderRepository.save(order);
    }

    /**
     * Возвращает все заказы текущего пользователя (новые сверху).
     *
     * @return {@link List}&lt;{@link Order}&gt; — заказы отсортированы по createdAt
     *         DESC.
     */
    public List<Order> getUserOrders() {
        User user = currentUserService.getCurrentUser();
        // findByUserOrderByCreatedAtDesc() генерирует SQL с JOIN и ORDER BY.
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }
}
