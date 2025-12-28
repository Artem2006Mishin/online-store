package com.example.backend.controller;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.example.backend.repository.NewsRepository;
import com.example.backend.model.News;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.util.StringUtils;

@RestController
@RequestMapping("news")
@CrossOrigin(origins = "http://localhost:5173")
public class NewsController {
  private final NewsRepository newsRepository;

  public NewsController(NewsRepository newsRepository) {
    this.newsRepository = newsRepository;
  }

  @GetMapping("/getNews")
  public List<News> getNews() {
    return this.newsRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
  }

  @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public News createNews(
      @RequestParam("title") String title,
      @RequestParam("text") String text,
      @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
    News news = new News();
    news.setTitle(title);
    news.setText(text);

    if (image != null && !image.isEmpty()) {
      Path uploadDir = Paths.get("src/main/resources/static/images/news");
      Files.createDirectories(uploadDir);

      String originalName = StringUtils.cleanPath(image.getOriginalFilename());
      String filename = "news-" + System.currentTimeMillis() + "-" + originalName;

      Path target = uploadDir.resolve(filename);
      Files.copy(image.getInputStream(), target);

      String url = "/images/news/" + filename;
      news.setImageURL(url);
    }

    return this.newsRepository.save(news);
  }

  @DeleteMapping("/delete/{id}")
  public void deleteNews(@PathVariable Long id) {
    this.newsRepository.deleteById(id);
  }

  @PostMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public News updateNews(
      @PathVariable Long id,
      @RequestParam("title") String title,
      @RequestParam("text") String text,
      @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
    News news = this.newsRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found"));
    news.setTitle(title);
    news.setText(text);

    if (image != null && !image.isEmpty()) {
      Path uploadDir = Paths.get("src/main/resources/static/images/news");
      Files.createDirectories(uploadDir);

      String originalName = StringUtils.cleanPath(image.getOriginalFilename());
      String filename = "news-" + System.currentTimeMillis() + "-" + originalName;

      Path target = uploadDir.resolve(filename);
      Files.copy(image.getInputStream(), target);

      String url = "/images/news/" + filename;
      news.setImageURL(url);
    }

    return this.newsRepository.save(news);
  }
}
