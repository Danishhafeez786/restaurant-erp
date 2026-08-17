package com.devmasters.restaurant_erp.auth.handler;

import com.devmasters.restaurant_erp.auth.model.UserModel;
import com.devmasters.restaurant_erp.auth.model.searchCriteria.UserSearchRequest;
import com.devmasters.restaurant_erp.common.model.pagination.PageResult;
import com.devmasters.restaurant_erp.auth.service.UserService;
import com.devmasters.restaurant_erp.auth.transformer.UserTransformer;
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


    public PageResult<UserModel> getAllUsers(UserSearchRequest request) {
        return userService.getAllUsers(request);
    }

}
