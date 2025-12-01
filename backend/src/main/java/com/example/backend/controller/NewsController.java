package com.example.backend.controller;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.backend.repository.NewsRepository;
import com.example.backend.model.News;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:5173")
public class NewsController {
  private final NewsRepository newsRepository;

  public NewsController(NewsRepository newsRepository) {
    this.newsRepository = newsRepository;
  }

  @GetMapping
  public List<News> getNews() {
    return this.newsRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
  }
}
