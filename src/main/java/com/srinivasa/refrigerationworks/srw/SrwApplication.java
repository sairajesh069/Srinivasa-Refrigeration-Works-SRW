package com.srinivasa.refrigerationworks.srw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SrwApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrwApplication.class, args);
	}

}