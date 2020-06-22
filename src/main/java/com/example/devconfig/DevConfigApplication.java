package com.example.devconfig;

import java.time.LocalDateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DevConfigApplication {

    @GetMapping
    public Object index() {
        return LocalDateTime.now();
    }

    public static void main(String[] args) {
        SpringApplication.run(DevConfigApplication.class, args);
    }

}
