package com.example.backend.controller;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для получения текущего времени сервера.
 *
 * Предназначен для синхронизации фронтенда с сервером (например, для
 * отображения времени создания заказа).
 * Эндпоинт публичный (по SecurityConfig: /api/time permitAll()).
 */
@RestController
@RequestMapping("/api")
public class TimeController {

    /**
     * Возвращает текущее время сервера в формате ISO 8601 (UTC).
     *
     * Формат: "2025-12-29T22:19:00Z" (с миллисекундами и 'Z' для UTC).
     *
     * @return {@link ResponseEntity} с JSON {"serverTime":
     *         "2025-12-29T22:19:00.123Z"}.
     *         Всегда 200 OK, ошибок не выбрасывает.
     */
    @GetMapping("/time")
    public ResponseEntity<Map<String, String>> getServerTime() {
        // OffsetDateTime.now(ZoneOffset.UTC) возвращает UTC время в ISO 8601 формате.
        String now = OffsetDateTime.now(ZoneOffset.UTC).toString();
        return ResponseEntity.ok(Map.of("serverTime", now));
    }
}
