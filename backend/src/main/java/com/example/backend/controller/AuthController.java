package com.example.backend.controller;

import com.example.backend.dto.RegisterDto;
import com.example.backend.dto.UserDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AuthService;
import com.example.backend.service.CurrentUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService,
            CurrentUserService currentUserService,
            UserRepository userRepository) {
        this.authService = authService;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
    }

    // ---------- ЛОГИН: JSON ----------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        try {
            UserResponseDto response = authService.login(userDto);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + e.getMessage());
        }
    }

    // ---------- РЕГИСТРАЦИЯ: multipart/form-data с email, password, avatar
    // ----------
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        try {
            // 1. Собираем DTO и регистрируем пользователя (логика та же, что и раньше)
            var registerDto = new RegisterDto();
            registerDto.setEmail(email);
            registerDto.setPassword(password);

            UserResponseDto response = authService.register(registerDto);

            // 2. Если пришёл файл аватара — сохраняем его и прописываем avatarUrl
            if (avatar != null && !avatar.isEmpty()) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found after register"));

                Path uploadDir = Paths.get("src/main/resources/static/avatars");
                Files.createDirectories(uploadDir);

                String originalName = StringUtils.cleanPath(avatar.getOriginalFilename());
                String filename = "user-" + user.getId() + "-" + originalName;

                Path target = uploadDir.resolve(filename);
                Files.copy(avatar.getInputStream(), target,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                String url = "/avatars/" + filename;

                user.setAvatarUrl(url);
                userRepository.save(user);
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Avatar upload failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    // ---------- (по желанию) отдельный endpoint для аватарки ----------
    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            User user = currentUserService.getCurrentUser();

            Path uploadDir = Paths.get("src/main/resources/static/avatars");
            Files.createDirectories(uploadDir);

            String originalName = StringUtils.cleanPath(file.getOriginalFilename());
            String filename = "user-" + user.getId() + "-" + originalName;

            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            String url = "/avatars/" + filename;

            user.setAvatarUrl(url);
            userRepository.save(user);

            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to upload avatar");
        }
    }
}
