package com.learning.travelry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableAsync
public class TravelryApplication {

	private static final Logger logger = LoggerFactory.getLogger(TravelryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TravelryApplication.class, args);
	}

}
