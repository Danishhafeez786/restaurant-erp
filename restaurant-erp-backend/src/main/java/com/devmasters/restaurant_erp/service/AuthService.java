package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.dto.AuthResponse;
import com.devmasters.restaurant_erp.dto.LoginRequest;
import com.devmasters.restaurant_erp.dto.SignupRequest;
import com.devmasters.restaurant_erp.entity.User;
import com.devmasters.restaurant_erp.repository.UserRepository;
import com.devmasters.restaurant_erp.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponse signup(SignupRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Email already registered")
                    .build();
        }

        // Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Passwords do not match")
                    .build();
        }

        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .restaurantName(request.getRestaurantName())
                .phone(request.getPhone())
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser.getEmail());

        return AuthResponse.builder()
                .token(token)
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .restaurantName(savedUser.getRestaurantName())
                .message("User registered successfully")
                .success(true)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Invalid email or password")
                    .build();
        }

        User foundUser = user.get();
        
        if (!passwordEncoder.matches(request.getPassword(), foundUser.getPassword())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Invalid email or password")
                    .build();
        }

        String token = jwtTokenProvider.generateToken(foundUser.getEmail());

        return AuthResponse.builder()
                .token(token)
                .email(foundUser.getEmail())
                .firstName(foundUser.getFirstName())
                .lastName(foundUser.getLastName())
                .restaurantName(foundUser.getRestaurantName())
                .message("Login successful")
                .success(true)
                .build();
    }
}
