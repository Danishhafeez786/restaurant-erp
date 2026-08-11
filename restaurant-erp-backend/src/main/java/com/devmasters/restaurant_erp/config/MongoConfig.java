package com.devmasters.restaurant_erp.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    private static final String MONGO_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "restaurant_db";

    @Bean
    public MongoClient mongoClient() {

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(MONGO_URI))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, DATABASE_NAME);
    }
}

