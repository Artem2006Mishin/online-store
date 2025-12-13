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

// класс проверки и генерации jwt токенов

@Component
public class JwtService {

  // значение секретного ключа
  @Value("${jwt.secret}")
  private String secretKey;

  // время жизни ключа
  @Value("${jwt.expiration}")
  private long expirationTime;

  // создание криптографического ключа подписи
  private Key getSignKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  // метод генерации jwt токена с данной почтой
  public String generateToken(String email) {
    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(getSignKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  // достает почту из токена
  public String extractEmail(String token) {
    return extractAllClaims(token).getSubject();
  }

  // проверяет, что токен валидный
  public boolean validateToken(String token) {
    try {
      extractAllClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  // парсинг токена
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // проверяет, не истек ли токен
  public boolean isTokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }

  // дата истечения токена
  public Date getExpirationDate(String token) {
    return extractAllClaims(token).getExpiration();
  }
}
