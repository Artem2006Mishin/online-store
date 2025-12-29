package com.example.backend;

/*
 * Главный класс Spring Boot приложения.
 * Запускает встроенный Tomcat сервер + сканирует контроллеры, сервисы, репозитории.
 */
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Точка входа Spring Boot приложения.
 *
 * Автоматически:
 * - Сканирует @Controller, @Service, @Repository в пакете com.example.backend.
 * - Настраивает H2/PostgreSQL/MySQL (application.properties).
 * - Запускает Tomcat на порту 8080.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.backend.repository")
public class BackendApplication {

	/**
	 * Настраивает статические ресурсы (изображения) для доступа через /images/**.
	 *
	 * Позволяет фронтенду загружать аватары/фото товаров:
	 * http://localhost:8080/images/avatars/user-1.jpg
	 *
	 * @return WebMvcConfigurer для Spring MVC.
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				// Абсолютный путь к папке проекта (src/main/resources/static/images).
				String projectDir = System.getProperty("user.dir");
				String imagesPath = Paths.get(projectDir, "src", "main", "resources", "static", "images")
						.toAbsolutePath().toString();

				// Два источника файлов:
				// 1. file: — динамически сохранённые файлы (avatars, news, catalog).
				// 2. classpath: — статические файлы из JAR.
				registry.addResourceHandler("/images/**")
						.addResourceLocations("file:" + imagesPath + "/") // ./src/main/resources/static/images/
						.addResourceLocations("classpath:/static/images/"); // из JAR
			}
		};
	}

	/**
	 * Запускает Spring Boot приложение.
	 *
	 * Логи: "Started BackendApplication in X.XXX seconds (JVM running for Y.YYY)".
	 *
	 * @param args аргументы командной строки (например, --server.port=8081).
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
