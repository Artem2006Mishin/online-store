package com.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;

/**
 * Реализация Spring Security UserDetailsService для загрузки пользователей из
 * БД.
 *
 * Интегрируется с {@link com.example.backend.filter.JwtAuthFilter} и
 * {@link com.example.backend.config.SecurityConfig}.
 * Преобразует JPA User → Spring Security UserDetails.
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Загружает пользователя по email для аутентификации Spring Security.
     *
     * Вызывается:
     * 1. AuthenticationManager.authenticate() при login.
     * 2. JwtAuthFilter после validateToken().
     *
     * @param email email пользователя (username в Spring Security).
     * @return {@link UserDetails} с ролями и BCrypt паролем.
     * @throws UsernameNotFoundException если пользователь не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));

        // Spring Security User.builder() автоматически добавляет префикс "ROLE_" к
        // .roles()
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // username = email
                .password(user.getPassword()) // BCrypt пароль
                .roles(user.getRole()) // "USER" → "ROLE_USER"
                .build();
    }
}
