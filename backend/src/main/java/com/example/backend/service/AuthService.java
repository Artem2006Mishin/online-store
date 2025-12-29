package com.example.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.RegisterDto;
import com.example.backend.dto.UpdateProfileDto;
import com.example.backend.dto.UserDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

/**
 * Сервис аутентификации и управления профилем пользователя.
 *
 * Обрабатывает логин, регистрацию, обновление профиля и смену пароля.
 * Интегрируется с Spring Security (AuthenticationManager) и JWT.
 */
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    /**
     * Конструктор (избыточен при @Autowired полях).
     *
     * @param userRepository репозиторий пользователей.
     */
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Выполняет аутентификацию пользователя и возвращает JWT-токен.
     *
     * @param userDto учетные данные (email, password).
     * @return {@link UserResponseDto} с токеном и обновлёнными данными
     *         пользователя.
     * @throws BadCredentialsException при неверном email/пароле (ловится в
     *                                 контроллере → 401).
     */
    @Transactional
    public UserResponseDto login(UserDto userDto) {
        try {
            // Spring Security проверяет email + BCrypt пароль.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDto.getEmail(),
                            userDto.getPassword()));

            // Загружаем пользователя из БД.
            User user = userRepository.findByEmail(userDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found: " + userDto.getEmail()));

            // Инкрементируем счётчик входов.
            user.setLoginCount(user.getLoginCount() + 1);
            userRepository.save(user);

            // Генерируем JWT-токен.
            String token = jwtService.generateToken(userDto.getEmail());

            // Корректируем avatarUrl для фронтенда.
            String avatarUrl = formatAvatarUrl(user.getAvatarUrl());

            return new UserResponseDto(
                    user.getEmail(),
                    token,
                    user.getRole(),
                    user.getLoginCount(),
                    avatarUrl);

        } catch (BadCredentialsException e) {
            // Пробрасываем для обработки в контроллере (401 Unauthorized).
            throw e;
        }
    }

    /**
     * Регистрирует нового пользователя с автоматической авторизацией.
     *
     * @param registerDto данные регистрации (email, password).
     * @return {@link UserResponseDto} с токеном для немедленного входа.
     * @throws RuntimeException если email уже существует.
     */
    @Transactional
    public UserResponseDto register(RegisterDto registerDto) {
        // Проверяем уникальность email.
        Optional<User> existingUser = userRepository.findByEmail(registerDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException(
                    "User with email " + registerDto.getEmail() + " already exists");
        }

        // Создаём пользователя с BCrypt паролем.
        User user = new User();
        user.setEmail(registerDto.getEmail());
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("USER");
        user.setLoginCount(1); // Первая авторизация.

        // Сохраняем и генерируем токен.
        user = userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());

        String avatarUrl = formatAvatarUrl(user.getAvatarUrl());

        return new UserResponseDto(
                user.getEmail(),
                token,
                user.getRole(),
                user.getLoginCount(),
                avatarUrl);
    }

    /**
     * Частично обновляет профиль текущего пользователя.
     *
     * @param updateDto   новые данные (email/password, null = не менять).
     * @param currentUser текущий авторизованный пользователь.
     * @return {@link UserResponseDto} с новым токеном.
     * @throws RuntimeException если новый email уже занят.
     */
    @Transactional
    public UserResponseDto updateProfile(UpdateProfileDto updateDto, User currentUser) {
        // Обновляем email с проверкой уникальности.
        if (updateDto.getEmail() != null && !updateDto.getEmail().equals(currentUser.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(updateDto.getEmail());
            if (existingUser.isPresent()) {
                throw new RuntimeException("Пользователь с таким email уже существует");
            }
            currentUser.setEmail(updateDto.getEmail());
        }

        // Обновляем пароль (BCrypt).
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updateDto.getPassword());
            currentUser.setPassword(encodedPassword);
        }

        // Сохраняем изменения и генерируем новый токен.
        userRepository.save(currentUser);
        String token = jwtService.generateToken(currentUser.getEmail());

        String avatarUrl = formatAvatarUrl(currentUser.getAvatarUrl());

        return new UserResponseDto(
                currentUser.getEmail(),
                token,
                currentUser.getRole(),
                currentUser.getLoginCount(),
                avatarUrl);
    }

    /**
     * Обновляет пароль пользователя (админская операция).
     *
     * @param user        пользователь из БД.
     * @param newPassword новый пароль в plain text.
     * @throws RuntimeException если пароль пустой.
     */
    @Transactional
    public void updatePassword(User user, String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            throw new RuntimeException("Пароль не может быть пустым");
        }

        // Хешируем и сохраняем.
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    /**
     * Форматирует URL аватара для фронтенда (добавляет /images префикс).
     *
     * @param avatarUrl сырой URL из БД.
     * @return исправленный URL или исходный.
     */
    private String formatAvatarUrl(String avatarUrl) {
        if (avatarUrl != null && !avatarUrl.startsWith("/images")) {
            return avatarUrl.startsWith("/") ? "/images" + avatarUrl : "/images/" + avatarUrl;
        }
        return avatarUrl;
    }
}
