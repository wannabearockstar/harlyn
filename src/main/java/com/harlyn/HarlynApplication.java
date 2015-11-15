package com.harlyn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.harlyn.repository")
@EnableAutoConfiguration
public class HarlynApplication {

    public static void main(String[] args) {
        SpringApplication.run(HarlynApplication.class, args);
    }
}
