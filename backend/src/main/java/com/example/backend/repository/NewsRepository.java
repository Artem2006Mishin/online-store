package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {};
