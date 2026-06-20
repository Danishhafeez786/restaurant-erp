package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.User;
import com.devmasters.restaurant_erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(User entity) {
        userRepository.save(entity);
        return entity;
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


}
