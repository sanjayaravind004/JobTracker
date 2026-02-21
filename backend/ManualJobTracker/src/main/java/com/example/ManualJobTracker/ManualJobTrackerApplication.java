package com.example.ManualJobTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ManualJobTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManualJobTrackerApplication.class, args);
	}

}
