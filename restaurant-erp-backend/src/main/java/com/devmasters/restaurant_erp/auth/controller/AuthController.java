package com.devmasters.restaurant_erp.auth.controller;

import com.devmasters.restaurant_erp.auth.model.ChangePasswordRequest;
import com.devmasters.restaurant_erp.auth.model.LoginResponse;
import com.devmasters.restaurant_erp.auth.model.RefreshTokenRequest;
import com.devmasters.restaurant_erp.common.model.ApiResponse;
import com.devmasters.restaurant_erp.auth.handler.AuthHandler;
import com.devmasters.restaurant_erp.auth.model.LoginRequest;
import com.devmasters.restaurant_erp.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthHandler authHandler;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>>  login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authHandler.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth API is working fine 🚀");
    }

    @PostMapping("/change-password")
    public ResponseEntity<LoginResponse> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {

        String email = authentication.getName();

        LoginResponse response = authService.changePassword(email, request);

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

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
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
