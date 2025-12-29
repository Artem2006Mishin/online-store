package com.example.backend.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backend.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Фильтр Spring Security для проверки JWT-токенов в каждом запросе.
 *
 * Выполняется один раз на запрос (OncePerRequestFilter). Извлекает токен из
 * Authorization header,
 * валидирует его и устанавливает аутентифицированного пользователя в
 * SecurityContext.
 * Подключается в
 * {@link com.example.backend.config.SecurityConfig#securityFilterChain()}.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Определяет, нужно ли пропустить фильтр для данного запроса.
     *
     * @param request HTTP-запрос.
     * @return true если фильтр нужно пропустить (публичные пути, preflight).
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // Preflight-запросы (OPTIONS) всегда пропускаем.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()))
            return true;

        // Публичные эндпоинты (каталог НЕ публичный, требует авторизации).
        return path.startsWith("/auth/")
                || path.equals("/")
                || path.equals("/error")
                || path.startsWith("/images/")
                || path.startsWith("/static/");
    }

    /**
     * Основная логика фильтра: проверка JWT и установка пользователя в
     * SecurityContext.
     *
     * Выполняется для всех запросов, кроме публичных (shouldNotFilter=false).
     *
     * @param request     HTTP-запрос.
     * @param response    HTTP-ответ.
     * @param filterChain цепочка фильтров Spring Security.
     * @throws ServletException при ошибках Servlet API.
     * @throws IOException      при ошибках чтения/записи HTTP.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        try {
            // Проверяем наличие Bearer токена в заголовке.
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // Убираем "Bearer ".

                // Извлекаем email из токена.
                String email = jwtService.extractEmail(token);

                // Если токен валидный и SecurityContext пустой — аутентифицируем.
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtService.validateToken(token)) {
                        // Загружаем UserDetails из БД.
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                        // Создаём объект аутентификации Spring Security.
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                        // Добавляем детали запроса (IP, sessionId).
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // Устанавливаем аутентифицированного пользователя в контекст.
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception e) {
            // При любой ошибке (невалидный токен, expired) очищаем контекст.
            SecurityContextHolder.clearContext();
        }

        // Продолжаем цепочку фильтров (authorizeHttpRequests, контроллеры).
        filterChain.doFilter(request, response);
    }
}
