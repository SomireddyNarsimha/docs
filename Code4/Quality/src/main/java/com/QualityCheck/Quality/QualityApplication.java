package com.QualityCheck.Quality;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@SpringBootApplication
public class QualityApplication {

	public static void main(String[] args) {
		//use to create the db file which is used to store .db file
		File dbDirectory = new File("quality/db");
		if (!dbDirectory.exists()) {
			dbDirectory.mkdirs();
			System.out.println("Created 'db' directory for SQLite database.");
		}
		SpringApplication.run(QualityApplication.class, args);
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
