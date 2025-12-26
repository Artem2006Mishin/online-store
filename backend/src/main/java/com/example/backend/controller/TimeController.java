package com.example.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TimeController {

    @GetMapping("/time")
    public ResponseEntity<Map<String, String>> getServerTime() {
        // Время сервера в ISO 8601 (UTC+0); можно поменять зону при необходимости
        String now = OffsetDateTime.now(ZoneOffset.UTC).toString();
        return ResponseEntity.ok(Map.of("serverTime", now));
    }
}
