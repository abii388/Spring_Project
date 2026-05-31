package com.microservice.quizz_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class QuizzServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizzServiceApplication.class, args);
	}

}
