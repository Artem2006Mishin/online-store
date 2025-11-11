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

public interface UserRepository extends JpaRepository<User, Long> {}