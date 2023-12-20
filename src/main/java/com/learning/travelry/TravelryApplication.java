package com.learning.travelry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class TravelryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelryApplication.class, args);
	}

}
