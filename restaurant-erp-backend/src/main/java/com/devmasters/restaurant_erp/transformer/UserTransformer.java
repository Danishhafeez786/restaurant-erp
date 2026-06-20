package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.model.UserModel;
import com.devmasters.restaurant_erp.model.LoginResponse;
import com.devmasters.restaurant_erp.model.UpdateProfileRequest;
import com.devmasters.restaurant_erp.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Component
@AllArgsConstructor
public class UserTransformer extends Transformer<User, UserModel>{
    private final PasswordEncoder passwordEncoder;

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
        if(model == null)
            return null;
        return User.builder()
                .id(UUID.randomUUID())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .email(model.getEmail())
                .password(passwordEncoder.encode(model.getPassword()))
                .phone(model.getPhone())
                .address(model.getAddress())
                .city(model.getCity())
                .state(model.getState())
                .zip(model.getZip())
                .referredBy(model.getReferredBy())
                .referralCode(UUID.randomUUID().toString().substring(0, 8))
                .role(model.getRole())
                .isActive(model.getIsActive())
                .build();
    }

    @Override
    public UserModel toModel(User entity) {
        if(entity == null)
            return null;
        return UserModel.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .city(entity.getCity())
                .state(entity.getState())
                .zip(entity.getZip())
                .referredBy(entity.getReferredBy())
                .referralCode(entity.getReferralCode())
                .role(entity.getRole())
                .isActive(entity.getIsActive())
                .build();
    }

    @Override
    public List<UserModel> toModels(List<User> entities) {
        if (entities == null) return Collections.emptyList();

        return entities.stream()
                .map(this::toModel)
                .toList();
    }
}

