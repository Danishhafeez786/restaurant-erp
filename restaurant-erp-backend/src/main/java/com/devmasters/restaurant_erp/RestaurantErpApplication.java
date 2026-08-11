package com.devmasters.restaurant_erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;


@SpringBootApplication
@EnableMongoAuditing
public class RestaurantErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantErpApplication.class, args);
	}

	@Bean
	CommandLineRunner checkMongoDatabase(MongoTemplate mongoTemplate) {
		return args -> {
			System.out.println("=================================");
			System.out.println("Mongo Database: " + mongoTemplate.getDb().getName());
			System.out.println("Mongo Collections: " + mongoTemplate.getCollectionNames());
			System.out.println("=================================");
		};
	}
}
