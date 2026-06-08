# Backend Configuration Files

## application.properties

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/
spring.application.name=restaurant-erp

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/restaurant_erp
spring.data.mongodb.username=
spring.data.mongodb.password=
spring.data.mongodb.authentication-database=admin

# JPA/Hibernate Configuration
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Jackson Configuration
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.time-zone=Asia/Kolkata
spring.jackson.default-property-inclusion=non_null

# Logging Configuration
logging.level.root=INFO
logging.level.com.devmasters.restaurant_erp=DEBUG
logging.file.name=logs/application.log

# JWT Configuration
jwt.secret=your-secret-key-here-make-it-very-long-and-random
jwt.expiration=86400000
jwt.refresh-token-expiration=604800000

# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# File Upload Configuration
file.upload.dir=/uploads/
file.upload.max-size=10MB

# Actuator Configuration
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=when-authorized
```

## application-dev.properties

```properties
# Development Environment
spring.profiles.active=dev

# MongoDB Development
spring.data.mongodb.uri=mongodb://localhost:27017/restaurant_erp_dev

# Logging
logging.level.org.springframework.web=DEBUG
logging.level.com.devmasters.restaurant_erp=DEBUG

# CORS
cors.allowed-origins=http://localhost:5173,http://localhost:3000
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allowed-headers=*
cors.allow-credentials=true
cors.max-age=3600

# JWT
jwt.secret=dev-secret-key-change-in-production
jwt.expiration=3600000
```

## application-prod.properties

```properties
# Production Environment
spring.profiles.active=prod

# MongoDB Production
spring.data.mongodb.uri=${MONGODB_URI}
spring.data.mongodb.username=${MONGODB_USER}
spring.data.mongodb.password=${MONGODB_PASSWORD}

# Logging
logging.level.root=WARN
logging.level.com.devmasters.restaurant_erp=INFO

# CORS
cors.allowed-origins=${ALLOWED_ORIGINS}
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allow-credentials=true
cors.max-age=3600

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
jwt.refresh-token-expiration=604800000

# Email
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}

# File Upload
file.upload.dir=/var/uploads/
file.upload.max-size=50MB

# Server
server.compression.enabled=true
server.compression.min-response-size=1024
```

## pom.xml Dependencies Reference

```xml
<!-- Core Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- MongoDB -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

<!-- Security & JWT -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- WebSocket -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>

<!-- Email -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>

<!-- Actuator -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- MongoDB Embedded for Testing -->
<dependency>
    <groupId>de.flapdoodle.embed</groupId>
    <artifactId>de.flapdoodle.embed.mongo</artifactId>
    <scope>test</scope>
</dependency>
```

## WebSocket Configuration (example)

```java
package com.devmasters.restaurant_erp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/kitchen").setAllowedOrigins("*");
        registry.addEndpoint("/ws/delivery").setAllowedOrigins("*");
    }
}
```

## CORS Configuration (example)

```java
package com.devmasters.restaurant_erp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

## Security Configuration (example)

```java
package com.devmasters.restaurant_erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## Docker Configuration (example)

### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/restaurant-erp-*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV MONGODB_URI=mongodb://mongo:27017/restaurant_erp

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'

services:
  mongodb:
    image: mongo:latest
    container_name: restaurant-mongo
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_ROOT_USERNAME: root
      MONGO_INITDB_ROOT_PASSWORD: password
    volumes:
      - mongo-data:/data/db

  backend:
    build: .
    container_name: restaurant-backend
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      MONGODB_URI: mongodb://root:password@mongodb:27017/restaurant_erp?authSource=admin
      JWT_SECRET: ${JWT_SECRET}
    depends_on:
      - mongodb

  frontend:
    image: node:18-alpine
    container_name: restaurant-frontend
    ports:
      - "5173:5173"
    working_dir: /app
    volumes:
      - ./restaurant-erp-frontend:/app
    command: npm run dev

volumes:
  mongo-data:
```

## Environment Variables (.env.example)

```bash
# Backend
JAVA_HOME=/usr/lib/jvm/java-17-openjdk
SPRING_PROFILES_ACTIVE=dev
MONGODB_URI=mongodb://localhost:27017/restaurant_erp
MONGODB_USER=root
MONGODB_PASSWORD=password

# JWT
JWT_SECRET=your-super-secret-jwt-key-change-in-production

# Mail
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# CORS
ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000

# File Upload
FILE_UPLOAD_DIR=/uploads/
FILE_UPLOAD_MAX_SIZE=50MB

# Payment Gateway (Stripe)
STRIPE_API_KEY=sk_live_your_key
STRIPE_WEBHOOK_SECRET=whsec_your_secret

# Payment Gateway (Razorpay)
RAZORPAY_KEY_ID=key_id
RAZORPAY_KEY_SECRET=key_secret
```

## Maven Build Configuration (example)

```bash
# Build JAR
mvn clean package

# Build with specific profile
mvn clean package -P prod

# Run locally
mvn spring-boot:run

# Run with dev profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Create Docker image
mvn clean package dockerfile:build
```

## Performance Tuning

### MongoDB Indexes
```javascript
// In MongoDB shell or MongoDB Compass
db.orders.createIndex({ "branchId": 1, "createdAt": -1 })
db.orders.createIndex({ "orderNumber": 1 })
db.orders.createIndex({ "status": 1, "branchId": 1 })
db.kitchen_orders.createIndex({ "stationId": 1, "status": 1 })
db.attendance.createIndex({ "employeeId": 1, "date": -1 })
db.inventory.createIndex({ "branchId": 1, "status": 1 })
```

### Connection Pooling
```properties
# MongoDB
spring.data.mongodb.pool-size=10
spring.data.mongodb.connect-timeout=5000
spring.data.mongodb.socket-timeout=5000

# Tomcat
server.tomcat.max-threads=200
server.tomcat.min-spare-threads=10
```

---

End of Configuration Reference
