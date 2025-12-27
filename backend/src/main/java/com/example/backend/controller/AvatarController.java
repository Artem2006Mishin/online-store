package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class AvatarController {

    private final CurrentUserService currentUserService;

    public AvatarController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

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
            currentUserService.save(user); // ← вот здесь запись в БД

            return ResponseEntity.ok().body(url);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to upload avatar");
        }
    }

}
