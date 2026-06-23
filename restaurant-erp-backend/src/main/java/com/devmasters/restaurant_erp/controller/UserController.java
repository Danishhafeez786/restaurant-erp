package com.devmasters.restaurant_erp.controller;

import com.devmasters.restaurant_erp.handler.UserHandler;
import com.devmasters.restaurant_erp.model.UserModel;
import com.devmasters.restaurant_erp.model.UserSearchRequest;
import com.devmasters.restaurant_erp.model.pagination.PageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserHandler userHandler;

    @PostMapping("/create")
    public ResponseEntity<UserModel> create(@Valid @RequestBody UserModel request) {
        UserModel response = userHandler.create(request);
        if (response == null)
            return ResponseEntity.badRequest().body(response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResult<UserModel>> getAllUsers(@RequestBody UserSearchRequest request) {
        return ResponseEntity.ok(userHandler.getAllUsers(request));
    }

}
