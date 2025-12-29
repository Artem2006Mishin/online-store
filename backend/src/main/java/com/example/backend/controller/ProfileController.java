package com.example.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.dto.UpdateProfileDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AuthService;
import com.example.backend.service.CurrentUserService;

/**
 * REST-контроллер для управления профилем пользователя и админских операций с
 * пользователями.
 *
 * Поддерживает:
 * - Получение профиля текущего пользователя + списка всех пользователей
 * (публичный).
 * - Обновление профиля (email, пароль, аватар) для текущего пользователя.
 * - Админские операции: удаление пользователей, смена пароля/роли (кроме себя).
 */
@RestController
@RequestMapping("")
public class ProfileController {

    private final CurrentUserService currentUserService;
    private final AuthService authService;
    private final UserRepository userRepository;

    /**
     * Конструктор внедряет сервисы для работы с текущим пользователем,
     * аутентификацией и репозиторий.
     *
     * @param currentUserService сервис для получения текущего авторизованного
     *                           пользователя из SecurityContext.
     * @param authService        сервис для обновления профиля и пароля (с
     *                           хешированием).
     * @param userRepository     репозиторий для доступа к сущности {@link User}.
     */
    public ProfileController(CurrentUserService currentUserService, AuthService authService,
            UserRepository userRepository) {
        this.currentUserService = currentUserService;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    /**
     * Возвращает данные текущего пользователя + список всех пользователей.
     *
     * Эндпоинт публичный (по SecurityConfig: /profile permitAll()).
     * Корректирует avatarUrl, если путь некорректный.
     *
     * @return {@link ResponseEntity}:
     *         <ul>
     *         <li>200 OK с Map{"currentUser": UserResponse, "allUsers":
     *         List&lt;User&gt;} при успехе.</li>
     *         <li>401 Unauthorized (пустое тело) если нет авторизованного
     *         пользователя.</li>
     *         </ul>
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            User user = currentUserService.getCurrentUser();
            String avatarUrl = user.getAvatarUrl();

            // Исправляем некорректные пути к аватару (добавляем /images префикс).
            if (avatarUrl != null && !avatarUrl.startsWith("/images")) {
                avatarUrl = avatarUrl.startsWith("/") ? "/images" + avatarUrl : "/images/" + avatarUrl;
            }

            // Формируем полный ответ: текущий + все пользователи.
            Map<String, Object> response = new HashMap<>();
            response.put("currentUser", new UserResponse(
                    user.getEmail(), null, user.getRole(), user.getLoginCount(), avatarUrl));
            response.put("allUsers", userRepository.findAll());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Нет пользователя в SecurityContext → 401.
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * Частично обновляет профиль текущего пользователя (email, пароль, аватар).
     *
     * Ожидает multipart/form-data с опциональными полями.
     *
     * @param email    новый email (опционально).
     * @param password новый пароль (опционально).
     * @param avatar   новый файл аватара (опционально).
     * @return {@link ResponseEntity}:
     *         <ul>
     *         <li>200 OK с обновлённым {@link UserResponseDto} при успехе.</li>
     *         <li>409 Conflict если email уже существует.</li>
     *         <li>400 Bad Request при других бизнес-ошибках.</li>
     *         <li>500 Internal Server Error при ошибках файловой системы.</li>
     *         </ul>
     * @throws IOException при ошибке сохранения аватара.
     */
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

            // Сохраняем новый аватар, если передан.
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

                // Обновляем ответ с новым avatarUrl.
                response = new UserResponseDto(
                        currentUser.getEmail(),
                        response.getToken(),
                        currentUser.getRole(),
                        currentUser.getLoginCount(),
                        currentUser.getAvatarUrl());
            }

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Email уже существует → 409 Conflict.
            if (e.getMessage().contains("уже существует")) {
                return ResponseEntity.status(409).body(e.getMessage());
            }
            // Другие бизнес-ошибки → 400 Bad Request.
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (IOException e) {
            // Ошибка файловой системы → 500.
            return ResponseEntity.status(500).body("Avatar upload failed: " + e.getMessage());
        } catch (Exception e) {
            // Непредвиденные ошибки → 500.
            return ResponseEntity.status(500).body("Update failed: " + e.getMessage());
        }
    }

    /**
     * Удаляет пользователя по ID (админская операция).
     *
     * Запрещено удалять себя. Доступ только авторизованным (ADMIN).
     *
     * @param id ID пользователя для удаления.
     * @return {@link ResponseEntity}:
     *         <ul>
     *         <li>200 OK при успешном удалении.</li>
     *         <li>400 Bad Request "Нельзя удалить себя".</li>
     *         <li>500 Internal Server Error при ошибках БД.</li>
     *         </ul>
     */
    @DeleteMapping("/profile/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            // Запрет на самоудаление.
            if (id.equals(currentUserService.getCurrentUser().getId())) {
                return ResponseEntity.badRequest().body("Нельзя удалить себя");
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка удаления: " + e.getMessage());
        }
    }

    /**
     * Меняет пароль пользователя по ID (админская операция).
     *
     * Запрещено менять свой пароль. Ожидает JSON {"password": "newpass"}.
     *
     * @param id   ID пользователя.
     * @param body JSON с полем "password".
     * @return {@link ResponseEntity}:
     *         <ul>
     *         <li>200 OK "Пароль изменён" при успехе.</li>
     *         <li>400 Bad Request "Нельзя менять свой пароль..." или 404 если
     *         пользователь не найден.</li>
     *         <li>500 Internal Server Error при ошибках.</li>
     *         </ul>
     */
    @PutMapping("/profile/users/{id}/password")
    public ResponseEntity<?> changeUserPassword(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            // Запрет на смену своего пароля через админку.
            if (id.equals(currentUserService.getCurrentUser().getId())) {
                return ResponseEntity.badRequest().body("Нельзя менять свой пароль через админку");
            }

            String newPassword = body.get("password");
            User user = userRepository.findById(id).orElse(null);

            if (user != null) {
                authService.updatePassword(user, newPassword);
                return ResponseEntity.ok().body("Пароль изменён");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Меняет роль пользователя по ID (админская операция).
     *
     * Запрещено менять свою роль. Ожидает JSON {"role": "ADMIN"}.
     *
     * @param id   ID пользователя.
     * @param body JSON с полем "role".
     * @return {@link ResponseEntity}:
     *         <ul>
     *         <li>200 OK "Роль изменена" при успехе.</li>
     *         <li>400 Bad Request при отсутствии роли или попытке смены своей
     *         роли.</li>
     *         <li>404 Not Found если пользователь не существует.</li>
     *         <li>500 Internal Server Error при ошибках.</li>
     *         </ul>
     */
    @PutMapping("/profile/users/{id}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            // Запрет на смену своей роли через админку.
            if (id.equals(currentUserService.getCurrentUser().getId())) {
                return ResponseEntity.badRequest().body("Нельзя менять свою роль через админку");
            }

            String newRole = body.get("role");
            if (newRole == null || newRole.isEmpty()) {
                return ResponseEntity.badRequest().body("Роль не указана");
            }

            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                user.setRole(newRole);
                userRepository.save(user);
                return ResponseEntity.ok().body("Роль изменена");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Record для ответа с данными текущего пользователя (без токена).
     *
     * Используется в getProfile() для сериализации в JSON.
     */
    public record UserResponse(String email,
            String token,
            String role,
            int loginCount,
            String avatarUrl) {
    }
}
