package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

import java.util.Optional;

import com.example.backend.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
  private UserRepository repository;
  private PasswordEncoder encoder;
  private JwtService jwtService;

  public UserController(UserRepository repository, PasswordEncoder encoder, JwtService jwtService) {
    this.repository = repository;
    this.encoder = encoder;
    this.jwtService = jwtService;
  }

  @PostMapping("/register") 
  public ResponseEntity<AuthResponceDto> register(@RequestBody RegisterRequestDto request) {
    if (repository.findByEmail(request.getEmail()).isPresent()) {
      return ResponseEntity.status(409).build(); // email уже занят
    }

    String encodedPassword = encoder.encode(request.getPassword());
    User newUser = new User(request.getEmail(), encodedPassword);

    if(request.getProfileImage() != null) {
      newUser.setProfileImage(request.getProfileImage());
    }

    repository.save(newUser);

    String accessToken = jwtService.generateAccessToken(newUser);
    String refreshToken = jwtService.generateRefreshToken(newUser);

    newUser.addRefreshToken(refreshToken);
    repository.save(newUser);

    AuthResponceDto responce = new AuthResponceDto(
            newUser.getEmail(),
            newUser.getRole(),
            accessToken,
            refreshToken,
            newUser.getProfileImage(),
            newUser.getVisitCount()
    );

    return ResponseEntity.status(201).body(responce);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponceDto> login(@RequestBody LoginRequestDto request) {
    Optional<User> findUser = repository.findByEmail(request.getEmail());
    if (findUser.isEmpty()) {
      return ResponseEntity.status(404).build(); // пользователь не найден
    }

    User user = findUser.get();
    if (!encoder.matches(request.getPassword(), user.getPassword())) {
      return ResponseEntity.status(401).build(); // неправильный пароль
    }

    user.incrementVisitCount();

    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    user.addRefreshToken(refreshToken);
    repository.save(user);

    AuthResponceDto responce = new AuthResponceDto(
            user.getEmail(),
            user.getRole(),
            accessToken,
            refreshToken,
            user.getProfileImage(),
            user.getVisitCount()
    );

    return ResponseEntity.ok(responce);
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponceDto> refresh(@RequestBody RefreshTokenRequestDto request) {
    try {
      String refreshToken = request.getRefreshToken();

      if(!jwtService.isRefreshToken(refreshToken)) {
        return ResponseEntity.status(401).build();
      }

      String email = jwtService.extractEmail(refreshToken);
      Optional<User> userOpt = repository.findByEmail(email);

      if(userOpt.isEmpty()) {
        return ResponseEntity.status(404).build();
      }

      User user = userOpt.get();

      if(!user.getRefreshTokens().contains(refreshToken)) {
        return ResponseEntity.status(401).build();
      }

      String newAccessToken = jwtService.generateAccessToken(user);
      String newRefreshToken = jwtService.generateRefreshToken(user);

      user.removeRefreshToken(refreshToken);
      user.addRefreshToken(newRefreshToken);
      repository.save(user);

      AuthResponceDto responce = new AuthResponceDto(
              user.getEmail(),
              user.getRole(),
              newAccessToken,
              newRefreshToken,
              user.getProfileImage(),
              user.getVisitCount()
      );

      return ResponseEntity.ok(responce);
    } catch (Exception e) {
      return ResponseEntity.status(401).build();
    }
  }

  @PostMapping("/logout")
  public ResponseEntity <?> logout(@RequestHeader("Authorization") String authHeader,
                                   @RequestBody RefreshTokenRequestDto request) {
    try {
      String token = authHeader.substring(7);
      String email = jwtService.extractEmail(token);

      Optional<User> userOpt = repository.findByEmail(email);
      if(userOpt.isPresent()) {
        User user = userOpt.get();
        user.removeRefreshToken(request.getRefreshToken());
        repository.save(user);
      }

      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.status(401).build();
    }
  }

  @PostMapping("/create-admin")
  public ResponseEntity<?> createAdmin(@RequestBody RegisterRequestDto request) {
    if(repository.findByEmail(request.getEmail()).isPresent()) {
      return ResponseEntity.status(409).build();
    }

    String encodedPassword = encoder.encode(request.getPassword());
    User admin = new User(request.getEmail(), encodedPassword, "ADMIN");
    repository.save(admin);

    return ResponseEntity.ok("Admin created");
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
