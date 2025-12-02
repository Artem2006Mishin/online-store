package com.example.backend.config;

import com.example.backend.filter.JwtAuthenticationFilter;
import com.example.backend.service.UserService;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // так называемый конфигурационный файл безопасности
    // ЧТО ДЕЛАЕТ ???????
    // - настраивает публичные и приватные URL
    // - обработка аутентификации
    // - настройки кросс-доменных запросов
    // - настройка сессий
    // - подключение JWT фильтра


    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserService userService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserService userService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // разрешаем запросы с фронтенда
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new CorsConfiguration();
                    corsConfig.addAllowedOrigin(String.valueOf(List.of("https://localhost:5173")));
                    corsConfig.addAllowedMethod(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS").toString());
                    corsConfig.addAllowedHeader(String.valueOf(List.of("*")));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))

                // настройка доступа к URL
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/hello").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/moderator/**").hasRole("MODERATOR")
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "MODERATOR", "ADMIN")
                        .anyRequest().authenticated()
                )

                // делаем приложение без сессий
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // подключаем фильтр
                .authenticationProvider(authentificationProvider())
                .addFilterBefore((Filter) jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authentificationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // настройка кодировщика паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
