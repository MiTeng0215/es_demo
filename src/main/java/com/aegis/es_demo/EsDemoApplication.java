package com.aegis.es_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class EsDemoApplication {

public static void main(String[] args) {
        SpringApplication.run(EsDemoApplication.class, args);
        }
}
