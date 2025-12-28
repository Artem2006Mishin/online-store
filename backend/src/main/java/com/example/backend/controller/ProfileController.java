package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class ProfileController {

    private final CurrentUserService currentUserService;

    public ProfileController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
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
                    user.getRole(),
                    user.getLoginCount(),
                    avatarUrl));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    public record UserResponse(String email,
            String role,
            int loginCount,
            String avatarUrl) {
    }

}
