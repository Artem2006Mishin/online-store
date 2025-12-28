package com.example.backend;

/*
 * 1. SpringApplication - это класс, который запускает приложение
 * 2. SpringBootApplication - это аннотация, говорящая, что это главный класс
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.backend.repository")
public class BackendApplication {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				// Получаем абсолютный путь к директории проекта
				String projectDir = System.getProperty("user.dir");
				String imagesPath = Paths.get(projectDir, "src", "main", "resources", "static", "images").toAbsolutePath().toString();
				
				// Для файлов, сохраненных во время выполнения (используем абсолютный путь)
				registry.addResourceHandler("/images/**")
						.addResourceLocations("file:" + imagesPath + "/")
						.addResourceLocations("classpath:/static/images/");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
