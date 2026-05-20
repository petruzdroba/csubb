package org.zdroba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("org.zdroba.entity")
public class RestApp {
    public static void main(String[] args) {
        SpringApplication.run(RestApp.class, args);
    }
}