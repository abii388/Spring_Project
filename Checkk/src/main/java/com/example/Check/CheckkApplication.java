package com.example.Check;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.Check.Go", "Controller"})
public class CheckkApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheckkApplication.class, args);
	}

}
