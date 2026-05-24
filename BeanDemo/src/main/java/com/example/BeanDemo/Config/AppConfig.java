package com.example.BeanDemo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MyService myService() {
        System.out.println("👉 myService() method called — creating the bean");
        return new MyService();
    }
}

