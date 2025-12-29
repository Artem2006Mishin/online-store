package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.backend.filter.JwtAuthFilter;
import com.example.backend.service.UserDetailServiceImpl;

/**
 * Конфигурация Spring Security для всего backend-приложения.
 * 
 * Отвечает за:
 * - Настройку авторизации и аутентификации (JWT, UserDetailsService, BCrypt).
 * - Определение публичных и защищённых маршрутов.
 * - Настройку CORS, CSRF и политики сессий.
 * - Подключение фильтров безопасности.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * собственный фильтр, который перехватывает запросы, извлекает JWT из
   * заголовка, проверяет его, и добавляет данные пользователя в SecurityContext
   */
  private final JwtAuthFilter jwtAuthFilter;

  /**
   * сервис, предоставляющий Spring Security информацию о пользователе (логин,
   * роли, пароль) при аутентификации
   */
  private final UserDetailServiceImpl userDetailService;

  /**
   * Конструктор внедряет зависимости фильтра JWT и сервиса пользователей.
   *
   * @param jwtAuthFilter     фильтр, который проверяет JWT-токены в каждом
   *                          запросе.
   * @param userDetailService кастомная реализация Spring UserDetailsService.
   */
  public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailServiceImpl userDetailService) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.userDetailService = userDetailService;
  }

  /**
   * Определяет основную цепочку фильтров и правила безопасности для
   * HTTP-запросов.
   *
   * @param http объект типа HttpSecurity, предоставляемый Spring Security.
   *             Используется для задания конфигурации фильтров, доступа, CORS и
   *             пр.
   * @return объект {@link SecurityFilterChain}, описывающий полную цепочку
   *         фильтров.
   * @throws Exception если возникает ошибка конфигурации (например, при конфликте
   *                   фильтров или запретах доступа).
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        // Разрешаем CORS на уровне SecurityConfig.
        .cors(cors -> {
        })
        // Отключаем CSRF, так как приложение stateless и работает по JWT.
        .csrf(csrf -> csrf.disable())
        // Устанавливаем stateless — сервер не хранит сессии.
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Настраиваем авторизацию по маршрутам.
        .authorizeHttpRequests(auth -> auth
            // Preflight (OPTIONS) всегда разрешён.
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            // Публичные маршруты (без авторизации).
            .requestMatchers("/auth/**", "/", "/error", "/images/**", "/static/**", "/profile").permitAll()
            .requestMatchers("/api/time").permitAll()

            // Новости: GET всем, а создание/редактирование/удаление — модераторам и
            // администраторам.
            .requestMatchers(HttpMethod.GET, "/news/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/news/create").hasAnyRole("MODERATOR", "ADMIN")
            .requestMatchers(HttpMethod.POST, "/news/update/**").hasAnyRole("MODERATOR", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/news/delete/**").hasAnyRole("MODERATOR", "ADMIN")

            // Продукты — полный доступ только администраторам.
            .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/products").hasRole("ADMIN")

            // Просмотр по категориям — только авторизованным пользователям.
            .requestMatchers(HttpMethod.GET, "/products/category/**").authenticated()
            .requestMatchers(HttpMethod.GET, "/products/by-name").authenticated()

            // Любые другие маршруты — требуют авторизации.
            .anyRequest().authenticated())
        // Подключаем кастомный AuthenticationProvider.
        .authenticationProvider(authenticationProvider())
        // Добавляем JWT-фильтр перед стандартным фильтром авторизации.
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        // Настраиваем поведение при ошибках аутентификации.
        .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
          // Вернуть 401 Unauthorized с JSON телом.
          response.setStatus(401);
          response.setContentType("application/json;charset=UTF-8");
          response.getWriter().write("{\"error\":\"unauthorized\"}");
        }))
        .build();
  }

  /**
   * Настраивает DaoAuthenticationProvider, используемый для проверки логина и
   * пароля.
   *
   * @return настроенный бин {@link DaoAuthenticationProvider}, использующий
   *         UserDetailService и BCrypt.
   * @throws IllegalArgumentException если UserDetailService или PasswordEncoder
   *                                  не установлены.
   */
  @SuppressWarnings("deprecation")
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider p = new DaoAuthenticationProvider();
    // Указываем сервис, который загружает пользователей из БД.
    p.setUserDetailsService(userDetailService);
    // Устанавливаем алгоритм хеширования пароля.
    p.setPasswordEncoder(passwordEncoder());
    return p;
  }

  /**
   * Определяет алгоритм хеширования паролей.
   *
   * @return новый экземпляр {@link BCryptPasswordEncoder}.
   *         Ошибок не выбрасывает.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Позволяет вручную использовать AuthenticationManager, например в
   * AuthService.login().
   *
   * @param config конфигурация аутентификации, предоставляемая Spring.
   * @return {@link AuthenticationManager}, который используется для проверки
   *         логина и пароля.
   * @throws Exception при ошибках инициализации или конфликте
   *                   AuthenticationManager.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  /**
   * Настраивает CORS для фронтенда (например, React/Vite на localhost:5173).
   *
   * @return {@link CorsConfigurationSource}, используемый Spring Security для
   *         определения, какие домены, заголовки и методы разрешены.
   *         Ошибок не выбрасывает.
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration cfg = new CorsConfiguration();
    // Разрешаем определённый домен (фронтенд).
    cfg.addAllowedOrigin("http://localhost:5173");
    // Разрешаем любые заголовки (Authorization, Content-Type и т.д.).
    cfg.addAllowedHeader("*");
    // Разрешаем любые методы (GET, POST, PUT, DELETE, OPTIONS).
    cfg.addAllowedMethod("*");
    // Разрешаем передачу cookie и credentials.
    cfg.setAllowCredentials(true);

    // Регистрируем конфигурацию для всех путей.
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", cfg);
    return source;
  }
}
