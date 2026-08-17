package com.devmasters.restaurant_erp.auth.service;

import com.devmasters.restaurant_erp.auth.domain.User;
import com.devmasters.restaurant_erp.auth.model.UserModel;
import com.devmasters.restaurant_erp.auth.model.searchCriteria.UserSearchRequest;
import com.devmasters.restaurant_erp.common.model.pagination.PageResult;

public interface UserService {
    User create(User entity);
    Boolean existsByEmail(String email);

    PageResult<UserModel> getAllUsers(UserSearchRequest request);

}

