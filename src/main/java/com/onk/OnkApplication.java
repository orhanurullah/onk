package com.onk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Slf4j
@EnableAsync
public class OnkApplication {

	public static void main(String[] args) {
		log.info("App is starting...");
		SpringApplication.run(OnkApplication.class, args);
	}
}
