package com.devmasters.restaurant_erp.config;

import com.devmasters.restaurant_erp.enums.Role;
import com.devmasters.restaurant_erp.domain.User;
import com.devmasters.restaurant_erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        String adminEmail = "admin@restauranterp.com";

        if (!userRepository.existsByEmail(adminEmail)) {

            User admin = User.builder()
                    .id(UUID.randomUUID())
                    .email(adminEmail)
                    .password(passwordEncoder.encode("Admin@123"))
                    .firstName("Super")
                    .lastName("Admin")
                    .role(Role.SUPER_ADMIN)
                    .isActive(true)
                    .referralCode("SUPER001")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(admin);

            System.out.println("SUPER ADMIN CREATED");
        }
    }
}
