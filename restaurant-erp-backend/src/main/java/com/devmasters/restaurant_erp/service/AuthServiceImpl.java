package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.model.LoginRequest;
import com.devmasters.restaurant_erp.domain.RefreshToken;
import com.devmasters.restaurant_erp.domain.User;
import com.devmasters.restaurant_erp.repository.RefreshTokenRepository;
import com.devmasters.restaurant_erp.repository.UserRepository;
import com.devmasters.restaurant_erp.config.JwtTokenProvider;
import com.devmasters.restaurant_erp.transformer.UserTransformer;
import com.devmasters.restaurant_erp.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new AccessDeniedException("Invalid credentials");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AccessDeniedException("User not exist in this email.r"));

        if (!user.getIsActive()) {
            throw new AccessDeniedException("User is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AccessDeniedException("Incorrect Password");
        }

        invalidateOldRefreshTokens(user);
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        saveRefreshToken(user, refreshToken);

        return buildSuccessResponse(user, accessToken, refreshToken);
    }



    @Override
    public LoginResponse changePassword(String email, ChangePasswordRequest request) {

        User user = findUserByEmail(email);
        if (user == null) return error("User not found");

        if (!isOldPasswordValid(request, user))
            return error("Old password is incorrect");

        if (!isPasswordMatching(request))
            return error("Passwords do not match");

        updatePassword(user, request.getNewPassword());
        return success("Password changed successfully");
    }

    @Override
    public UserModel getCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserTransformer.toProfileResponse(user);
    }

    @Override
    public UserModel updateProfile(
            String email,
            UpdateProfileRequest request
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserTransformer.updateEntity(user, request);

        User savedUser = userRepository.save(user);

        return UserTransformer.toProfileResponse(savedUser);
    }

    @Override
    public void logout(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        user.setTokenVersion(user.getTokenVersion() + 1);

        userRepository.save(user);
    }

    @Override
    public LoginResponse refreshToken(
            RefreshTokenRequest request
    ) {

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByToken(request.getRefreshToken())
                        .orElse(null);

        if (refreshToken == null) {

            return LoginResponse.builder()
                    .success(false)
                    .message("Invalid refresh token")
                    .build();
        }

        if (refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            return LoginResponse.builder()
                    .success(false)
                    .message("Refresh token expired")
                    .build();
        }

        User user = userRepository.findById(
                refreshToken.getUserId()
        ).orElseThrow();

        String newAccessToken =
                jwtTokenProvider.generateAccessToken(user.getEmail(), user.getRole(), user.getTokenVersion());

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .success(true)
                .message("Token refreshed")
                .build();
    }





    private String generateAccessToken(User user) {
        return jwtTokenProvider.generateAccessToken(
                user.getEmail(),
                user.getRole(),
                user.getTokenVersion()
        );
    }



    private void invalidateOldRefreshTokens(User user) {
        refreshTokenRepository.deleteByUserId(user.getId());
    }


    private String generateRefreshToken(User user) {
        return jwtTokenProvider.generateRefreshToken(user.getEmail());
    }

    private void saveRefreshToken(User user, String refreshToken) {
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .id(UUID.randomUUID())
                        .token(refreshToken)
                        .userId(user.getId())
                        .expiryDate(LocalDateTime.now().plusDays(7))
                        .build()
        );
    }

    private LoginResponse buildFailureResponse() {
        return LoginResponse.builder()
                .success(false)
                .message("Invalid email or password")
                .build();
    }

    private LoginResponse buildSuccessResponse(User user, String accessToken, String refreshToken) {
        return UserTransformer.toAuthResponse(
                user,
                accessToken,
                refreshToken,
                "Login successful"
        );
    }


    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private boolean isOldPasswordValid(ChangePasswordRequest request, User user) {
        return passwordEncoder.matches(request.getOldPassword(), user.getPassword());
    }

    private boolean isPasswordMatching(ChangePasswordRequest request) {
        return request.getNewPassword().equals(request.getConfirmPassword());
    }

    private void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private LoginResponse success(String message) {
        return LoginResponse.builder()
                .success(true)
                .message(message)
                .build();
    }

    private LoginResponse error(String message) {
        return LoginResponse.builder()
                .success(false)
                .message(message)
                .build();
    }
}
