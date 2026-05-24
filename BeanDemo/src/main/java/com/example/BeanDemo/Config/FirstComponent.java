package com.example.BeanDemo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FirstComponent {

    @Autowired
    private MyService myService;

    public void run() {
        System.out.println("FirstComponent using: " + myService);
        myService.doWork();
    }
}