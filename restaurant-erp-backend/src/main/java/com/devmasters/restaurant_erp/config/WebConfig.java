package com.devmasters.restaurant_erp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://192.168.100.79:3000",
                        "http://192.168.100.38:3000", "http://192.168.100.223:3000",
                        "http://10.111.246.248:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
//                .allowCredentials(true);
    }
}
