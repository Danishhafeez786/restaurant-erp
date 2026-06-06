package com.devmasters.restaurant_erp.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    private static final String SECRET =
            "my-super-secret-key-change-this-to-long-random-string-restaurant-pos-2026";

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
}
