package com.example.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.model.User;
import com.example.backend.dto.RegisterDto;
import com.example.backend.dto.UpdateProfileDto;
import com.example.backend.dto.UserDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto login(UserDto userDto) {
        try {
            // проверка логина/пароля Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDto.getEmail(),
                            userDto.getPassword()));

            // если сюда дошли — креды верные
            User user = userRepository.findByEmail(userDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found: " + userDto.getEmail()));

            // увеличиваем счётчик входов
            user.setLoginCount(user.getLoginCount() + 1);
            userRepository.save(user);

            String token = jwtService.generateToken(userDto.getEmail());
            String avatarUrl = user.getAvatarUrl();
            // Если avatarUrl не null и не начинается с /images, добавляем префикс
            if (avatarUrl != null && !avatarUrl.startsWith("/images")) {
                avatarUrl = avatarUrl.startsWith("/") ? "/images" + avatarUrl : "/images/" + avatarUrl;
            }
            return new UserResponseDto(
                user.getEmail(), 
                token, 
                user.getRole(), 
                user.getLoginCount(), 
                avatarUrl
            );

        } catch (BadCredentialsException e) {
            // важно пробросить именно BadCredentialsException,
            // контроллер превратит его в 401
            throw e;
        }
    }

    @Transactional
    public UserResponseDto register(RegisterDto registerDto) {
        Optional<User> existingUser = userRepository.findByEmail(registerDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException(
                    "User with email " + registerDto.getEmail() + " already exists");
        }

        User user = new User();
        user.setEmail(registerDto.getEmail());
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("USER");
        user.setLoginCount(user.getLoginCount() + 1); // первая авторизация

        user = userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        String avatarUrl = user.getAvatarUrl();
        // Если avatarUrl не null и не начинается с /images, добавляем префикс
        if (avatarUrl != null && !avatarUrl.startsWith("/images")) {
            avatarUrl = avatarUrl.startsWith("/") ? "/images" + avatarUrl : "/images/" + avatarUrl;
        }
        return new UserResponseDto(
            user.getEmail(), 
            token, 
            user.getRole(), 
            user.getLoginCount(), 
            avatarUrl
        );
    }

    @Transactional
    public UserResponseDto updateProfile(UpdateProfileDto updateDto, User currentUser) {
        // Проверяем email, если он изменен
        if (updateDto.getEmail() != null && !updateDto.getEmail().equals(currentUser.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(updateDto.getEmail());
            if (existingUser.isPresent()) {
                throw new RuntimeException("Пользователь с таким email уже существует");
            }
            currentUser.setEmail(updateDto.getEmail());
        }

        // Обновляем пароль, если он предоставлен
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(updateDto.getPassword());
            currentUser.setPassword(encodedPassword);
        }

        userRepository.save(currentUser);

        String avatarUrl = currentUser.getAvatarUrl();
        // Если avatarUrl не null и не начинается с /images, добавляем префикс
        if (avatarUrl != null && !avatarUrl.startsWith("/images")) {
            avatarUrl = avatarUrl.startsWith("/") ? "/images" + avatarUrl : "/images/" + avatarUrl;
        }
        return new UserResponseDto(
            currentUser.getEmail(), 
            null, // token не нужен при обновлении
            currentUser.getRole(), 
            currentUser.getLoginCount(), 
            avatarUrl
        );
    }
}
