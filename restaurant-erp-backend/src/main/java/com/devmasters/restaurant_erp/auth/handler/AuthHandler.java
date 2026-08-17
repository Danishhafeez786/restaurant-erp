package com.devmasters.restaurant_erp.auth.handler;

import com.devmasters.restaurant_erp.auth.model.LoginRequest;
import com.devmasters.restaurant_erp.auth.model.LoginResponse;
import com.devmasters.restaurant_erp.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final AuthService authService;

    public LoginResponse login(@Valid LoginRequest request) {
        return authService.login(request);
    }
}
