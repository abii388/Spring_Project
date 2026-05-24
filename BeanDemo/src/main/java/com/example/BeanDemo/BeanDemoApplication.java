package com.example.BeanDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.BeanDemo.Config.FirstComponent;
import com.example.BeanDemo.Config.SecondComponent;

@SpringBootApplication 
public class BeanDemoApplication implements CommandLineRunner {
	   @Autowired
	    private FirstComponent firstComponent;

	    @Autowired
	    private SecondComponent secondComponent;

	public static void main(String[] args) {
		SpringApplication.run(BeanDemoApplication.class, args);
	}

    @Override
    public void run(String... args) {
        firstComponent.run();
        secondComponent.run();
    }
}
