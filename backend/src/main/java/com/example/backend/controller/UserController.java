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

  @PostMapping("/register") // TODO: сделать другой dto для регистрации, типа UserRegisterDto
  public ResponseEntity<UserResponseDto> register(@RequestBody UserDto request) { 
    if (repository.findByEmail(request.getEmail()).isPresent()) {
      return ResponseEntity.status(409).body(null); // email уже занят
    }

    String encodedPassword = encoder.encode(request.getPassword());
    User newUser = new User(request.getEmail(), encodedPassword);
    repository.save(newUser);

    String token = "test_token"; // TODO: сделать нормальный JWT токен
    UserResponseDto responseData = new UserResponseDto(newUser.getEmail(), token);
    return ResponseEntity.status(201).body(responseData); // успешно создан
  }

  @PostMapping("/login") // TODO: сделать другой dto для регистрации, типа UserLoginDto
  public ResponseEntity<UserResponseDto> login(@RequestBody UserDto request) {
    Optional<User> findUser = repository.findByEmail(request.getEmail());
    if (findUser.isEmpty()) {
      return ResponseEntity.status(404).body(null); // пользователь не найден
    }

    User user = findUser.get();
    if (!encoder.matches(request.getPassword(), user.getPassword())) {
      return ResponseEntity.status(401).body(null); // неправильный пароль
    }

    String token = "test_token"; // TODO: сделать нормальный JWT токен
    UserResponseDto response = new UserResponseDto(user.getEmail(), token);

    return ResponseEntity.status(200).body(response); // успех в логине!!!!!
  }
}
