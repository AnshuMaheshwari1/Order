package com.anshu.writer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OrderWriterApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderWriterApplication.class, args);
	}

}
