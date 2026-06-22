package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.User;
import com.devmasters.restaurant_erp.model.UserModel;
import com.devmasters.restaurant_erp.model.UserSearchRequest;
import com.devmasters.restaurant_erp.model.pagination.PageResult;

public interface UserService {
    User create(User entity);
    Boolean existsByEmail(String email);

    PageResult<UserModel> getAllUsers(UserSearchRequest request);

}

