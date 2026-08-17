package com.devmasters.restaurant_erp.auth.service;

import com.devmasters.restaurant_erp.auth.model.*;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse changePassword(String email, ChangePasswordRequest request);

    UserModel getCurrentUser(String email);

    LoginResponse refreshToken(RefreshTokenRequest request);

    void logout(String refreshToken);
}