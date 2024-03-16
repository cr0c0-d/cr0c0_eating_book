package me.croco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EatingBooksApplication {
    public static void main(String[] args) {
        SpringApplication.run(EatingBooksApplication.class, args);
    }
}
