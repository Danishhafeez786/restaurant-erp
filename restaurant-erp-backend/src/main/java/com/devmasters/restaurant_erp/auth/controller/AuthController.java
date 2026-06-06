package com.devmasters.restaurant_erp.auth.controller;

import com.devmasters.restaurant_erp.auth.model.*;
import com.devmasters.restaurant_erp.auth.repository.RefreshTokenRepository;
import com.devmasters.restaurant_erp.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
public class AuthController {

    private final AuthService authService;

    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {

        AuthResponse response = authService.signup(request);

        if (!response.isSuccess())
            return ResponseEntity.badRequest().body(response);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);

        if (!response.isSuccess())
            return ResponseEntity.status(401).body(response);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth API is working fine 🚀");
    }

    @PostMapping("/change-password")
    public ResponseEntity<AuthResponse> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        AuthResponse response = authService.changePassword(email, request);

        if (!response.isSuccess())
            return ResponseEntity.badRequest().body(response);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {

        if (authentication == null)
            return ResponseEntity.status(401)
                    .body("Not authenticated");

        return ResponseEntity.ok(
                authService.getCurrentUser(authentication.getName())
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @RequestBody UpdateProfileRequest request,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                authService.updateProfile(authentication.getName(), request)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(
                authService.refreshToken(request)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {

        authService.logout(authentication.getName());

        return ResponseEntity.ok("Logged out successfully");
    }
}
