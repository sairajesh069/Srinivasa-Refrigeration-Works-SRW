package com.srinivasa.refrigerationworks.srw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/*
 * Main application class to start the Spring Boot application with caching enabled.
 */
@SpringBootApplication
@EnableCaching
public class SrwApplication {

	/*
	 * Runs the Spring Boot application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SrwApplication.class, args);
	}
}