package com.devmasters.restaurant_erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class RestaurantErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantErpApplication.class, args);
	}

}
