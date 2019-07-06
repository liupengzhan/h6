package com.example.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Demo6Application {
	static final Logger logger=LoggerFactory.getLogger(com.example.demo.Demo6Application.class);

	public static void main(String[] args) {
		logger.info("spingboot加载");
		SpringApplication.run(Demo6Application.class, args);
		logger.info("springboot加载完毕");
	}

}
