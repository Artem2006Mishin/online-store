package com.example.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.dto.RegisterDto;
import com.example.backend.dto.UserDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AuthService;
import com.example.backend.service.CurrentUserService;

/**
 * REST-контроллер для операций аутентификации и регистрации пользователей.
 *
 * Обрабатывает:
 * - Логин по email и паролю (JSON).
 * - Регистрацию с возможной загрузкой аватара (multipart/form-data).
 * - Отдельную загрузку/смену аватара для текущего авторизованного пользователя.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    /**
     * Конструктор внедряет сервисы аутентификации, текущего пользователя и
     * репозиторий пользователей.
     *
     * @param authService        сервис для логина и регистрации (работа с JWT и
     *                           учетными данными).
     * @param currentUserService сервис для получения текущего аутентифицированного
     *                           пользователя.
     * @param userRepository     репозиторий для доступа к сущности User в базе
     *                           данных.
     */
    public AuthController(AuthService authService,
            CurrentUserService currentUserService,
            UserRepository userRepository) {
        this.authService = authService;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
    }

    // ---------- ЛОГИН: JSON ----------

    /**
     * Выполняет аутентификацию пользователя по email и паролю.
     *
     * Ожидает JSON с полями учетных данных (например, email и password) и при
     * успешном входе
     * возвращает DTO с токеном и данными пользователя.
     *
     * @param userDto объект с учетными данными пользователя (email, пароль и т.п.).
     * @return {@link ResponseEntity}:
     *         <ul>
     *         <li>200 OK c {@link UserResponseDto} при успешном логине.</li>
     *         <li>401 Unauthorized c текстом "Invalid email or password" при
     *         неверных данных.</li>
     *         <li>500 Internal Server Error c сообщением "Login failed: ..." при
     *         других ошибках.</li>
     *         </ul>
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        try {
            // Выполняем логин через сервис аутентификации.
            UserResponseDto response = authService.login(userDto);
            // Успешный ответ с токеном и данными пользователя.
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            // Неверный логин/пароль → 401 Unauthorized.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        } catch (Exception e) {
            // Любая другая непредвиденная ошибка → 500 Internal Server Error.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + e.getMessage());
        }
    }

    // ---------- РЕГИСТРАЦИЯ: multipart (email, password, avatar) ----------

    /**
     * Регистрирует нового пользователя и опционально загружает его аватар.
     *
     * Принимает multipart/form-data:
     * - email (обязательный текстовый параметр).
     * - password (обязательный текстовый параметр).
     * - avatar (необязательный файл-аватар).
     *
     * При успешной регистрации создает пользователя, генерирует токен
     * и, если передан файл, сохраняет аватар на диск и обновляет URL в профиле
     * пользователя.
     *
     * @param email    email нового пользователя.
     * @param password пароль нового пользователя (в незахешированном виде, далее
     *                 шифруется в сервисе).
     * @param avatar   файл аватара; может быть null или пустым.
     * @return {@link ResponseEntity}:
     *         <ul>
     *         <li>200 OK c {@link UserResponseDto} (email, роль, токен, счётчик
     *         логинов, avatarUrl) при успехе.</li>
     *         <li>409 Conflict c текстом "Error: ..." при логических ошибках
     *         (например, email уже существует).</li>
     *         <li>500 Internal Server Error c текстом "Avatar upload failed: ..."
     *         при ошибках файловой системы.</li>
     *         <li>500 Internal Server Error c текстом "Registration failed: ..."
     *         при других ошибках.</li>
     *         </ul>
     * @throws RuntimeException может быть выброшен из сервиса/репозитория
     *                          (например, при ошибке поиска пользователя).
     * @throws IOException      при ошибках чтения/записи файла аватара на диск.
     */
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        try {
            // Формируем DTO для регистрации из примитивных параметров.
            RegisterDto registerDto = new RegisterDto();
            registerDto.setEmail(email);
            registerDto.setPassword(password);

            // Регистрируем пользователя и получаем базовый ответ (без аватара).
            UserResponseDto response = authService.register(registerDto);

            // Если аватар передан и не пустой — сохраняем его и обновляем пользователя.
            if (avatar != null && !avatar.isEmpty()) {
                // Ищем только что созданного пользователя по email.
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found after register"));

                // Директория для аватаров.
                Path uploadDir = Paths.get("src/main/resources/static/images/avatars");
                Files.createDirectories(uploadDir);

                // Очищаем имя файла от потенциально опасных символов.
                String originalName = StringUtils.cleanPath(avatar.getOriginalFilename());
                // Формируем уникальное имя файла с ID пользователя.
                String filename = "user-" + user.getId() + "-" + originalName;

                // Путь назначения и копирование файла на диск (перезапись при совпадении).
                Path target = uploadDir.resolve(filename);
                Files.copy(avatar.getInputStream(), target,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // URL для доступа к аватару из фронта.
                String url = "/images/avatars/" + filename;

                // Сохраняем URL аватара у пользователя.
                user.setAvatarUrl(url);
                user = userRepository.save(user);

                // Обновляем DTO ответа, чтобы вернуть актуальное avatarUrl.
                response = new UserResponseDto(
                        user.getEmail(),
                        response.getToken(),
                        user.getRole(),
                        user.getLoginCount(),
                        user.getAvatarUrl());
            }

            // Возвращаем успешный ответ с данными пользователя и токеном.
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Логические/бизнес-ошибки (например, пользователь уже существует) → 409
            // Conflict.
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: " + e.getMessage());
        } catch (IOException e) {
            // Ошибки ввода-вывода при работе с файлом аватара → 500.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Avatar upload failed: " + e.getMessage());
        } catch (Exception e) {
            // Любые другие неожиданные ошибки → 500.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    // ---------- (опционально) смена аватарки после регистрации ----------

    /**
     * Загружает или изменяет аватар текущего авторизованного пользователя.
     *
     * Ожидает multipart/form-data с файлом:
     * - file (обязательное поле MultipartFile).
     *
     * Определяет текущего пользователя через {@link CurrentUserService},
     * сохраняет файл на диск и обновляет поле avatarUrl в сущности User.
     *
     * @param file новый файл аватара пользователя.
     * @return {@link ResponseEntity}:
     *         <ul>
     *         <li>200 OK с текстом URL-адреса аватара при успешном сохранении.</li>
     *         <li>400 Bad Request с текстом "File is empty" если файл не передан
     *         или пуст.</li>
     *         <li>500 Internal Server Error с текстом "Failed to upload avatar" при
     *         ошибке записи файла.</li>
     *         </ul>
     * @throws IOException при ошибке чтения входящего файла или записи на диск.
     */
    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        // Валидация на пустой файл до обращения к диску или базе.
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            // Получаем текущего авторизованного пользователя из контекста безопасности.
            User user = currentUserService.getCurrentUser();

            // Готовим директорию для сохранения аватаров, создаем при необходимости.
            Path uploadDir = Paths.get("src/main/resources/static/images/avatars");
            Files.createDirectories(uploadDir);

            // Очищаем оригинальное имя файла.
            String originalName = StringUtils.cleanPath(file.getOriginalFilename());
            // Формируем уникальное имя файла на основе ID пользователя.
            String filename = "user-" + user.getId() + "-" + originalName;

            // Путь к файлу и запись с возможной заменой существующего.
            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            // Публичный URL для доступа к аватару.
            String url = "/images/avatars/" + filename;

            // Обновляем URL аватара у пользователя и сохраняем изменения.
            user.setAvatarUrl(url);
            userRepository.save(user);

            // Возвращаем клиенту URL нового аватара.
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            // Ошибка работы с файловой системой → 500.
            return ResponseEntity.internalServerError()
                    .body("Failed to upload avatar");
        }
    }
}
