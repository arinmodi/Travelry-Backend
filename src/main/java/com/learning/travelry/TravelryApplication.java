package com.learning.travelry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableAsync
public class TravelryApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelryApplication.class, args);
	}

}
