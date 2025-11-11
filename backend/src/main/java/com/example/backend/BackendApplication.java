package com.example.backend;

/*
 * 1. SpringApplication - это класс, который запускает приложение
 * 2. SpringBootApplication - это аннотация, говорящая, что это главный класс
 */
import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
