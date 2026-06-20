package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.model.LoginResponse;
import com.devmasters.restaurant_erp.model.UserModel;
import com.devmasters.restaurant_erp.service.UserService;
import com.devmasters.restaurant_erp.transformer.UserTransformer;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserHandler {
    private final UserService userService;
    private final UserTransformer userTransformer;

    public UserModel create(@Valid UserModel model) {
        if(!userService.existsByEmail(model.getEmail()))
            return userTransformer.toModel(userService.create(userTransformer.toEntity(model)));
        return null;
    }
}
