package com.Microservice2.Monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

@SpringBootApplication
@EnableScheduling
public class MonitoringApplication {

	public static void main(String[] args) {

		//use to create the db file which is used to store .db file
		File dbDirectory = new File("monitoring/db");
		if (!dbDirectory.exists()) {
			dbDirectory.mkdirs();
			System.out.println("Created 'db' directory for SQLite database.");
		}
		SpringApplication.run(MonitoringApplication.class, args);
	}

}
