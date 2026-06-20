package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.User;

public interface UserService {
    User create(User entity);
    Boolean existsByEmail(String email);

}

