package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CurrentUserService;
import com.example.backend.service.AuthService;
import com.example.backend.dto.UpdateProfileDto;
import com.example.backend.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("")
public class ProfileController {

    private final CurrentUserService currentUserService;
    private final AuthService authService;
    private final UserRepository userRepository;

    public ProfileController(CurrentUserService currentUserService, AuthService authService, UserRepository userRepository) {
        this.currentUserService = currentUserService;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile() {
        try {
            User user = currentUserService.getCurrentUser();
            String avatarUrl = user.getAvatarUrl();
            // Если avatarUrl не null и не начинается с /images, добавляем префикс
            if (avatarUrl != null && !avatarUrl.startsWith("/images")) {
                avatarUrl = avatarUrl.startsWith("/") ? "/images" + avatarUrl : "/images/" + avatarUrl;
            }
            return ResponseEntity.ok(new UserResponse(
                    user.getEmail(),
                    null,
                    user.getRole(),
                    user.getLoginCount(),
                    avatarUrl));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        try {
            UpdateProfileDto updateDto = new UpdateProfileDto();
            updateDto.setEmail(email);
            updateDto.setPassword(password);

            User currentUser = currentUserService.getCurrentUser();
            UserResponseDto response = authService.updateProfile(updateDto, currentUser);

            if (avatar != null && !avatar.isEmpty()) {
                Path uploadDir = Paths.get("src/main/resources/static/images/avatars");
                Files.createDirectories(uploadDir);

                String originalName = StringUtils.cleanPath(avatar.getOriginalFilename());
                String filename = "user-" + currentUser.getId() + "-" + originalName;

                Path target = uploadDir.resolve(filename);
                Files.copy(avatar.getInputStream(), target,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                String url = "/images/avatars/" + filename;

                currentUser.setAvatarUrl(url);
                currentUser = userRepository.save(currentUser);

                // Обновляем response с новым avatarUrl
                response = new UserResponseDto(
                    currentUser.getEmail(),
                    response.getToken(),
                    currentUser.getRole(),
                    currentUser.getLoginCount(),
                    currentUser.getAvatarUrl()
                );
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("уже существует")) {
                return ResponseEntity.status(409).body(e.getMessage());
            }
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Avatar upload failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Update failed: " + e.getMessage());
        }
    }

    public record UserResponse(String email,
            String token,
            String role,
            int loginCount,
            String avatarUrl) {
    }

}
