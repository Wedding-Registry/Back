package com.wedding.serviceapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ServiceapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceapiApplication.class, args);
	}

}
