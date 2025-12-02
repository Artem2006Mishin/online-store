package com.example.backend.repository;

/*
 * UserRepository - это интерфейс который дает тебе все готовые методы
 * для работы с таблицей users в базе данных. Также я могу внутри {} 
 * определить свои методы для общения с таблицей
 * 
 * JpaRepository - интерфейс с готовыми методами
 */

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.User;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}