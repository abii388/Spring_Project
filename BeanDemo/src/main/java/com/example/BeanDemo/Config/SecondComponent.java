package com.example.BeanDemo.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecondComponent {

    @Autowired
    private MyService myService;

    public void run() {
        System.out.println("SecondComponent using: " + myService);
        myService.doWork();
    }
}