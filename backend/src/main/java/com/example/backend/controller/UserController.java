package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.dto.UserDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.repository.UserRepository;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
  private UserRepository repository;
  private PasswordEncoder encoder;

  public UserController(UserRepository repository, PasswordEncoder encoder) {
    this.repository = repository;
    this.encoder = encoder;
  }

  @PostMapping("/register") 
  public ResponseEntity<UserResponseDto> register(@RequestBody UserDto request) { 
    if (repository.findByEmail(request.getEmail()).isPresent()) {
      return ResponseEntity.status(409).body(null); // email уже занят
    }

    String encodedPassword = encoder.encode(request.getPassword());
    User newUser = new User(request.getEmail(), encodedPassword);
    repository.save(newUser);

    String token = "test_token";
    UserResponseDto responseData = new UserResponseDto(newUser.getEmail(), token);
    return ResponseEntity.status(201).body(responseData); // успешно создан
  }

  @PostMapping("/login")
  public ResponseEntity<UserResponseDto> login(@RequestBody UserDto request) {
    Optional<User> findUser = repository.findByEmail(request.getEmail());
    if (findUser.isEmpty()) {
      return ResponseEntity.status(404).body(null); // пользователь не найден
    }

    User user = findUser.get();
    if (!encoder.matches(request.getPassword(), user.getPassword())) {
      return ResponseEntity.status(401).body(null); // неправильный пароль
    }

    String token = "test_token";
    UserResponseDto response = new UserResponseDto(user.getEmail(), token);

    return ResponseEntity.status(200).body(response); // успех в логине!!!!!
  }
}

/*
  FIXME: надо сделать разные DTO для логина и регистрации. 

  объясню почему: сейчас у пользователя есть только поля email и password.
  и когда я отправляю данные с фронта с email и password, то мой 
  @RequestBody парсит json и получает пользователя. 

  но когда у пользователя будет больше полей (фотка и кол-во посещений) при 
  регистрации я отправлю json с такими данными, то @RequestBody для register создаст такого пользователя основе них, а вот уже Login уже не сможет уже так сделать, так 
  как у него будут только email password.
*/

// FIXME: сделать нормальный JWT токен а не говно, что сейчас.
