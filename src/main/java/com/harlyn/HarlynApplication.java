package com.harlyn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class HarlynApplication {

	public static void main(String[] args) {
		SpringApplication.run(HarlynApplication.class, args);
	}
}
