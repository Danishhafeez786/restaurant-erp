package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.model.SignupRequest;
import com.devmasters.restaurant_erp.model.LoginResponse;
import com.devmasters.restaurant_erp.model.UpdateProfileRequest;
import com.devmasters.restaurant_erp.model.UserModel;
import com.devmasters.restaurant_erp.enums.Role;
import com.devmasters.restaurant_erp.domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserTransformer extends Transformer<User, UserModel>{

    // DTO → ENTITY
    public static User toEntity(SignupRequest request, String encodedPassword) {

        String referralCode = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        return User.builder()
                .id(UUID.randomUUID())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encodedPassword)
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zip(request.getZip())
                .referredBy(request.getReferredBy())
                .referralCode(referralCode)
                .role(Role.CUSTOMER)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ENTITY → RESPONSE
    public static LoginResponse toAuthResponse(User user,
                                               String accessToken,
                                               String refreshToken,
                                               String message) {

        return LoginResponse.builder()
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

    public static UserModel toProfileResponse(User user) {

        return UserModel.builder()
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

    @Override
    public User toEntity(UserModel model) {

        return null;
    }

    @Override
    public UserModel toModel(User entity) {
        return null;
    }
}
