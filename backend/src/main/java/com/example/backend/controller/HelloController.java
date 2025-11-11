package com.example.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
/* 
 * Класс-контроллер - класс, который умеет обрабатывать HTTP-запросы от клиентов
 * 1. @GetMapping - указывает на какой URL GET-запроса реагировать
 * 2. @RestController - аннотация, позволяющая классу работать с REST API 
 * 3. @RequestParam - аннотация говорит spring: "возьми параметры из URL и положи 
 * их в переменную"
 * 4. @CrossOrigin(origins = "http://localhost:5173") - аннотация, разрешающая кросс-платформенные
 * запросы: когда React делает запрос с http://localhost:5173 к Spring, то браузер блокирует
 * такие запросы из-за безопасности, поэтому добавляем 
 * 
 * Map.of() - способ создания неизменяемых словарей
 */
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class HelloController {
  @GetMapping("/hello")
  public String greet() {
    return "Backend запущен успешно!";
  }
}
