package com.devmasters.restaurant_erp.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI restaurantOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Restaurant ERP API")
                                .description("Restaurant ERP & POS Management System")
                                .version("v1.0")
                                .contact(
                                        new Contact()
                                                .name("Dev Masters")
                                                .email("support@devmasters.com")
                                )
                                .license(
                                        new License()
                                                .name("Private License")
                                )
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Restaurant ERP Documentation")
                );
    }
}