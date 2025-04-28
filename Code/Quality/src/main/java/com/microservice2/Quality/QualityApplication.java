package com.microservice2.Quality;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
public class QualityApplication {

	public static void main(String[] args) {
		File dbDirectory = new File("./Quality/db");
		if (!dbDirectory.exists()) {
			dbDirectory.mkdirs();
			System.out.println("Created 'db' directory for SQLite database.");
		}
		SpringApplication.run(QualityApplication.class, args);
	}

}
