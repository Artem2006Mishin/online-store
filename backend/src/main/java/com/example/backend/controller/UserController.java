package com.example.backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

import java.util.List;

/*
 * @RestController - говорим, что обрабатываем HTTP-запросы и возвращаем данные
 * @RequestMapping - говорим, что все методы в этом классе будут доступны
 * по адресу http://localhost:8080/users"
 * @CrossOrigin(origins = "http://localhost:5173") - разрешаем запросы от фронтенда
 * на порту 5173
 * @GetMapping - обрабатываем GET-запросы
 * @PostMapping - обрабатываем POST-запросы
 * @RequestBody - превращаем из JSON в Java-объект
 */

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping
  public User postMethodName(@RequestBody User user) {
    return userRepository.save(user);    
  }

  @GetMapping
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }
}
