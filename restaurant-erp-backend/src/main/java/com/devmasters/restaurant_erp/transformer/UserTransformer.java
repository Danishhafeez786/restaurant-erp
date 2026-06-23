package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.User;
import com.devmasters.restaurant_erp.model.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.UUID;


@Component
@AllArgsConstructor
public class UserTransformer extends Transformer<User, UserModel>{
    private final PasswordEncoder passwordEncoder;
    private final RoleTransformer roleTransformer;

    // ENTITY → RESPONSE
    public static LoginResponse toAuthResponse(User user, String accessToken,
                                               String refreshToken, String message) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().getRoleName())
                .referralCode(user.getReferralCode())
                .message(message)
                .success(true)
                .build();
    }

    @Override
    public User toEntity(UserModel model) {
        if(model == null)
            return null;
        return User.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .fullName(model.getFullName())
                .email(model.getEmail())
                .password(passwordEncoder.encode(model.getPassword()))
                .phone(model.getPhone())
                .referralCode(UUID.randomUUID().toString().substring(0, 8))
                .role(roleTransformer.toEntity(model.getRole()))
                .isActive(model.getIsActive())
                .build();
    }

    @Override
    public UserModel toModel(User entity) {
        if(entity == null)
            return null;
        return UserModel.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .referralCode(entity.getReferralCode())
                .role(roleTransformer.toModel(entity.getRole()))
                .isActive(entity.getIsActive())
                .build();
    }
}

