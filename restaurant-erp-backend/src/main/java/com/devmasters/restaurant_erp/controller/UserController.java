package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.UserHandler;
import com.devmasters.restaurant_erp.model.LoginResponse;
import com.devmasters.restaurant_erp.model.SignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserHandler userHandler;

    @PostMapping("/create")
    public ResponseEntity<LoginResponse> create(@Valid @RequestBody SignupRequest request) {

        LoginResponse response = userHandler.create(request);

        if (!response.isSuccess())
            return ResponseEntity.badRequest().body(response);

        return ResponseEntity.ok(response);
    }

}
