package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.User;
import com.devmasters.restaurant_erp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final UserRepository userRepository;

    public User validateUser(String email, int tokenVersion) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        if (user.getTokenVersion() != tokenVersion) {
            throw new BadCredentialsException("Invalid token version");
        }

        return user;
    }
}