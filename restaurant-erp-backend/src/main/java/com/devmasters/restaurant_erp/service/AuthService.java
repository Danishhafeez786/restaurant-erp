package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.model.LoginRequest;
import com.devmasters.restaurant_erp.model.*;

public interface AuthService {

    LoginResponse signup(SignupRequest request);

    LoginResponse login(LoginRequest request);

    LoginResponse changePassword(
            String email,
            ChangePasswordRequest request
    );

    UserModel getCurrentUser(String email);

    UserModel updateProfile(
            String email,
            UpdateProfileRequest request
    );


    LoginResponse refreshToken(
            RefreshTokenRequest request
    );

    void logout(String refreshToken);
}