package com.example.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.model.News;
import com.example.backend.repository.NewsRepository;

/**
 * REST-контроллер для полного CRUD-управления новостями.
 *
 * Поддерживает:
 * - Получение всех новостей (публичный доступ, отсортировано по ID ASC).
 * - Создание новости с опциональной картинкой (multipart/form-data).
 * - Удаление новости по ID.
 * - Обновление новости с возможной заменой картинки (multipart/form-data).
 *
 * Требует авторизации MODERATOR/ADMIN для операций create/update/delete (по
 * SecurityConfig).
 */
@RestController
@RequestMapping("news")
@CrossOrigin(origins = "http://localhost:5173")
public class NewsController {

  private final NewsRepository newsRepository;

  /**
   * Конструктор внедряет репозиторий новостей для доступа к БД.
   *
   * @param newsRepository Spring Data JPA репозиторий для сущности {@link News}.
   */
  public NewsController(NewsRepository newsRepository) {
    this.newsRepository = newsRepository;
  }

  /**
   * Возвращает все новости, отсортированные по возрастанию ID.
   *
   * Эндпоинт публичный (GET /news/** разрешён всем по SecurityConfig).
   *
   * @return {@link List}&lt;{@link News}&gt; — список всех новостей в порядке
   *         возрастания ID.
   *         Пустой список, если таблица пуста.
   */
  @GetMapping("/getNews")
  public List<News> getNews() {
    // findAll(Sort.by(Sort.Direction.ASC, "id")) генерирует SQL: ORDER BY id ASC.
    return this.newsRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
  }

  /**
   * Создает новую новость с опциональной загрузкой изображения.
   *
   * Ожидает multipart/form-data:
   * - title (обязательный текст).
   * - text (обязательный текст).
   * - image (необязательный файл).
   *
   * Требует роль MODERATOR или ADMIN (по SecurityConfig).
   *
   * @param title название новости.
   * @param text  содержимое новости.
   * @param image файл изображения (может быть null/пустым).
   * @return сохранённая сущность {@link News} с автогенерированным ID.
   * @throws IOException при ошибке записи файла изображения на диск.
   */
  @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public News createNews(
      @RequestParam("title") String title,
      @RequestParam("text") String text,
      @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
    // Создаём новую сущность новости.
    News news = new News();
    news.setTitle(title);
    news.setText(text);

    // Если изображение передано — сохраняем его на диск.
    if (image != null && !image.isEmpty()) {
      // Директория для изображений новостей.
      Path uploadDir = Paths.get("src/main/resources/static/images/news");
      Files.createDirectories(uploadDir);

      // Очищаем имя файла + уникальный префикс с timestamp.
      String originalName = StringUtils.cleanPath(image.getOriginalFilename());
      String filename = "news-" + System.currentTimeMillis() + "-" + originalName;

      // Копируем файл с заменой при необходимости.
      Path target = uploadDir.resolve(filename);
      Files.copy(image.getInputStream(), target);

      // Устанавливаем публичный URL изображения.
      String url = "/images/news/" + filename;
      news.setImageURL(url);
    }

    // Сохраняем новость в БД (автогенерация ID).
    return this.newsRepository.save(news);
  }

  /**
   * Удаляет новость по её ID.
   *
   * Требует роль MODERATOR или ADMIN (по SecurityConfig).
   * Если новости с таким ID нет — Spring Data JPA просто ничего не удаляет (без
   * ошибки).
   *
   * @param id идентификатор новости для удаления.
   */
  @DeleteMapping("/delete/{id}")
  public void deleteNews(@PathVariable Long id) {
    // deleteById() выполняет DELETE FROM news WHERE id = ?.
    this.newsRepository.deleteById(id);
  }

  /**
   * Обновляет существующую новость по ID, включая замену изображения.
   *
   * Ожидает multipart/form-data:
   * - title (новое название).
   * - text (новое содержимое).
   * - image (новое изображение, опционально).
   *
   * Требует роль MODERATOR или ADMIN (по SecurityConfig).
   *
   * @param id    идентификатор новости для обновления.
   * @param title новое название новости.
   * @param text  новое содержимое новости.
   * @param image новое изображение (может быть null/пустым).
   * @return обновлённая сущность {@link News}.
   * @throws RuntimeException если новость с указанным ID не найдена.
   * @throws IOException      при ошибке записи нового изображения на диск.
   */
  @PostMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public News updateNews(
      @PathVariable Long id,
      @RequestParam("title") String title,
      @RequestParam("text") String text,
      @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
    // Ищем новость или выбрасываем исключение.
    News news = this.newsRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("News not found"));

    // Обновляем текстовые поля.
    news.setTitle(title);
    news.setText(text);

    // Если новое изображение передано — заменяем старое.
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

    // Сохраняем обновления в БД.
    return this.newsRepository.save(news);
  }
}
