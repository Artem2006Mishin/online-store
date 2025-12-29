package com.example.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.model.Category;
import com.example.backend.model.Product;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.service.ProductService;

/**
 * REST-контроллер для полного CRUD-управления товарами каталога.
 *
 * Поддерживает:
 * - Поиск товара по имени (требует авторизации).
 * - Получение всех товаров (только ADMIN).
 * - Фильтрацию по категории (авторизованные пользователи).
 * - Создание/обновление/удаление товаров с изображениями (только ADMIN).
 */
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
  private final ProductRepository repository;

  @Autowired
  private ProductService productService;

  @Autowired
  private CategoryRepository categoryRepository;

  /**
   * Конструктор внедряет основной репозиторий товаров.
   * Дополнительные зависимости (@Autowired) — сервис и репозиторий категорий.
   *
   * @param repository Spring Data JPA репозиторий для сущности {@link Product}.
   */
  public ProductController(ProductRepository repository) {
    this.repository = repository;
  }

  /**
   * Поиск товара по точному имени через ProductService.
   *
   * Требует авторизации (по SecurityConfig: /products/by-name authenticated()).
   *
   * @param productName JSON-строка с именем товара для поиска.
   * @return {@link ResponseEntity} с найденным {@link Product} или 404 при
   *         ошибке.
   * @throws RuntimeException если товар не найден по ID (из сервиса).
   */
  @PostMapping("/by-name")
  public ResponseEntity<Product> getProductByName(@RequestBody String productName) {
    // Сервис возвращает ID товара по имени, затем ищем полную сущность.
    Long productId = productService.getProductIdByName(productName);
    Product product = repository.findById(productId)
        .orElseThrow(() -> new RuntimeException("Товар не найден"));
    return ResponseEntity.ok(product);
  }

  /**
   * Возвращает все товары каталога.
   *
   * Доступ только для ADMIN (по SecurityConfig: GET /products hasRole("ADMIN")).
   *
   * @return {@link List}&lt;{@link Product}&gt; — полный список всех товаров.
   */
  @GetMapping
  public List<Product> getAllProducts() {
    return repository.findAll();
  }

  /**
   * Возвращает товары определённой категории по названию.
   *
   * Требует авторизации (по SecurityConfig: /products/category/**
   * authenticated()).
   *
   * @param categoryName название категории (например, "Electronics").
   * @return {@link List}&lt;{@link Product}&gt; — товары данной категории.
   *         Пустой список, если категория не существует.
   */
  @GetMapping("/category/{categoryName}")
  public List<Product> getProductsByCategory(@PathVariable String categoryName) {
    // findByCategory_Title() генерирует JOIN с фильтром по названию категории.
    return repository.findByCategory_Title(categoryName);
  }

  /**
   * Создает новый товар с изображением и привязкой к категории.
   *
   * Ожидает multipart/form-data, доступ только ADMIN.
   *
   * @param name          название товара.
   * @param priceStr      цена как строка (парсится в double).
   * @param image         файл изображения (опционально).
   * @param categoryIdStr ID категории как строка (парсится в Long).
   * @return сохранённый {@link Product} с автогенерированным ID.
   * @throws RuntimeException при неверном формате цены/ID категории или
   *                          отсутствии категории.
   * @throws IOException      при ошибке сохранения изображения.
   */
  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Product createProduct(
      @RequestParam("name") String name,
      @RequestParam("price") String priceStr,
      @RequestParam(value = "image", required = false) MultipartFile image,
      @RequestParam("categoryId") String categoryIdStr) throws IOException {
    Product product = new Product();
    product.setName(name);

    // Парсим цену, выбрасываем ошибку при неверном формате.
    try {
      double price = Double.parseDouble(priceStr);
      product.setPrice(price);
    } catch (NumberFormatException e) {
      throw new RuntimeException("Неверный формат цены: " + priceStr);
    }

    // Парсим ID категории и находим сущность.
    try {
      Long categoryId = Long.valueOf(categoryIdStr);
      Category category = categoryRepository.findById(categoryId)
          .orElseThrow(() -> new RuntimeException("Категория не найдена"));
      product.setCategory(category);
    } catch (NumberFormatException e) {
      throw new RuntimeException("Неверный формат ID категории: " + categoryIdStr);
    }

    // Сохраняем изображение с уникальным именем.
    if (image != null && !image.isEmpty()) {
      Path uploadDir = Paths.get("src/main/resources/static/images/catalog");
      Files.createDirectories(uploadDir);

      String originalName = StringUtils.cleanPath(image.getOriginalFilename());
      String filename = "product-" + System.currentTimeMillis() + "-" + originalName;

      Path target = uploadDir.resolve(filename);
      Files.copy(image.getInputStream(), target);

      String url = "/images/catalog/" + filename;
      product.setImageURL(url);
    } else {
      product.setImageURL(null);
    }

    product.setIsCart(false);
    return repository.save(product);
  }

  /**
   * Частично обновляет существующий товар (только переданные поля).
   *
   * Ожидает multipart/form-data, доступ только ADMIN.
   *
   * @param id            ID товара для обновления.
   * @param name          новое название (опционально).
   * @param priceStr      новая цена как строка (опционально).
   * @param image         новое изображение (опционально).
   * @param categoryIdStr новый ID категории (опционально).
   * @return обновлённый {@link Product}.
   * @throws RuntimeException если товар не найден или неверный формат цены/ID.
   * @throws IOException      при ошибке сохранения изображения.
   */
  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Product updateProduct(
      @PathVariable Long id,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "price", required = false) String priceStr,
      @RequestParam(value = "image", required = false) MultipartFile image,
      @RequestParam(value = "categoryId", required = false) String categoryIdStr) throws IOException {
    // Ищем товар или выбрасываем ошибку.
    Product product = repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Товар не найден"));

    // Обновляем только переданные непустые поля.
    if (name != null && !name.isEmpty()) {
      product.setName(name);
    }

    if (priceStr != null && !priceStr.isEmpty()) {
      try {
        double price = Double.parseDouble(priceStr);
        product.setPrice(price);
      } catch (NumberFormatException e) {
        throw new RuntimeException("Неверный формат цены: " + priceStr);
      }
    }

    if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
      try {
        Long categoryId = Long.valueOf(categoryIdStr);
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        product.setCategory(category);
      } catch (NumberFormatException e) {
        throw new RuntimeException("Неверный формат ID категории: " + categoryIdStr);
      }
    }

    // Заменяем изображение, если передано новое.
    if (image != null && !image.isEmpty()) {
      Path uploadDir = Paths.get("src/main/resources/static/images/catalog");
      Files.createDirectories(uploadDir);

      String originalName = StringUtils.cleanPath(image.getOriginalFilename());
      String filename = "product-" + System.currentTimeMillis() + "-" + originalName;

      Path target = uploadDir.resolve(filename);
      Files.copy(image.getInputStream(), target);

      String url = "/images/catalog/" + filename;
      product.setImageURL(url);
    }

    return repository.save(product);
  }

  /**
   * Удаляет товар по ID.
   *
   * Доступ только ADMIN. Spring Data JPA не выбрасывает ошибку при отсутствии
   * записи.
   *
   * @param id ID товара для удаления.
   */
  @DeleteMapping("/{id}")
  public void deleteProduct(@PathVariable Long id) {
    repository.deleteById(id);
  }
}
