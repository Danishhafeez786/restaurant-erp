package com.devmasters.restaurant_erp.auth.service;

import com.devmasters.restaurant_erp.auth.model.*;

public interface AuthService {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse changePassword(
            String email,
            ChangePasswordRequest request
    );

    UserProfileResponse getCurrentUser(String email);

    UserProfileResponse updateProfile(
            String email,
            UpdateProfileRequest request
    );


    AuthResponse refreshToken(
            RefreshTokenRequest request
    );

    void logout(String refreshToken);
}