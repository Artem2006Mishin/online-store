package com.example.backend.dto;

import java.util.List;

/**
 * DTO для создания нового заказа.
 *
 * Содержит только список ID товаров, которые пользователь хочет купить.
 * Используется в
 * {@link com.example.backend.controller.OrderController#createOrder()}.
 */
public class CreateOrderDto {

    private List<Long> productIds;

    /**
     * Возвращает список ID товаров для заказа.
     *
     * @return {@link List}&lt;{@link Long}&gt; — ID товаров (не null, но может быть
     *         пустым).
     */
    public List<Long> getProductIds() {
        return productIds;
    }

    /**
     * Устанавливает список ID товаров для заказа.
     *
     * @param productIds список ID товаров из каталога.
     */
    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
