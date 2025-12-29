package com.example.backend.service;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Сервис для генерации, валидации и парсинга JWT-токенов.
 *
 * Используется в {@link com.example.backend.filter.JwtAuthFilter} и
 * {@link com.example.backend.service.AuthService}.
 * Конфигурация из application.properties: jwt.secret, jwt.expiration.
 */
@Component
public class JwtService {

  // Секретный ключ подписи (application.properties: jwt.secret).
  @Value("${jwt.secret}")
  private String secretKey;

  // Время жизни токена в миллисекундах (application.properties: jwt.expiration).
  @Value("${jwt.expiration}")
  private long expirationTime;

  /**
   * Создаёт криптографический ключ HMAC-SHA для подписи JWT.
   *
   * @return {@link Key} для подписи/проверки токена (HS256).
   */
  private Key getSignKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  /**
   * Генерирует новый JWT-токен для пользователя.
   *
   * @param email email пользователя (subject токена).
   * @return компактный JWT-токен (eyJhbGciOiJIUzI1NiJ9...).
   */
  public String generateToken(String email) {
    return Jwts.builder()
        .setSubject(email) // email как subject
        .setIssuedAt(new Date()) // время выдачи
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // время истечения
        .signWith(getSignKey(), SignatureAlgorithm.HS256) // подпись HS256
        .compact(); // финальный токен
  }

  /**
   * Извлекает email из JWT-токена.
   *
   * @param token валидный JWT-токен.
   * @return email пользователя или null при ошибке.
   */
  public String extractEmail(String token) {
    return extractAllClaims(token).getSubject();
  }

  /**
   * Проверяет валидность JWT-токена (подпись + формат).
   *
   * @param token JWT-токен для проверки.
   * @return true если токен валиден (подпись верна, не истёк).
   */
  public boolean validateToken(String token) {
    try {
      extractAllClaims(token); // проверка подписи + парсинг
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      // Неверный формат, подпись или expired → false.
      return false;
    }
  }

  /**
   * Парсит JWT-токен и возвращает claims (payload).
   *
   * @param token валидный JWT-токен.
   * @return {@link Claims} с данными токена (subject, expiration, issuedAt).
   * @throws JwtException при неверной подписи/формате.
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignKey()) // тот же ключ для проверки
        .build()
        .parseClaimsJws(token) // парсинг + проверка подписи
        .getBody(); // извлечение payload
  }

  /**
   * Проверяет, истёк ли токен.
   *
   * @param token JWT-токен.
   * @return true если expiration < current time.
   */
  public boolean isTokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }

  /**
   * Возвращает дату истечения токена.
   *
   * @param token JWT-токен.
   * @return {@link Date} expiration времени.
   */
  public Date getExpirationDate(String token) {
    return extractAllClaims(token).getExpiration();
  }
}
