package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.model.LoginResponse;
import com.devmasters.restaurant_erp.model.SignupRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class UserHandler {
    public LoginResponse create(@Valid SignupRequest request) {
        return null;
    }
}
