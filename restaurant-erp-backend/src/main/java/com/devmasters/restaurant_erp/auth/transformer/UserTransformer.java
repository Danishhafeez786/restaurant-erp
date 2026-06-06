package com.devmasters.restaurant_erp.auth.transformer;

import com.devmasters.restaurant_erp.auth.model.SignupRequest;
import com.devmasters.restaurant_erp.auth.model.AuthResponse;
import com.devmasters.restaurant_erp.auth.model.UpdateProfileRequest;
import com.devmasters.restaurant_erp.auth.model.UserProfileResponse;
import com.devmasters.restaurant_erp.auth.domain.Role;
import com.devmasters.restaurant_erp.auth.domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserTransformer {

    // DTO → ENTITY
    public static User toEntity(SignupRequest request, String encodedPassword) {

        String referralCode = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        return User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zip(request.getZip())
                .referredBy(request.getReferredBy())
                .referralCode(referralCode)
                .role(Role.CUSTOMER)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ENTITY → RESPONSE
    public static AuthResponse toAuthResponse(User user,
                                              String accessToken,
                                              String refreshToken,
                                              String message) {

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .referralCode(user.getReferralCode())
                .message(message)
                .success(true)
                .build();
    }

    public static UserProfileResponse toProfileResponse(User user) {

        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .state(user.getState())
                .zip(user.getZip())
                .referralCode(user.getReferralCode())
                .role(user.getRole())
                .build();
    }

    public static void updateEntity(
            User user,
            UpdateProfileRequest request
    ) {
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setZip(request.getZip());
        user.setUpdatedAt(LocalDateTime.now());
    }
}
