package com.urlive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@Profile("!test")
public class UrliveApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrliveApplication.class, args);
	}

}
