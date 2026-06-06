package com.devmasters.restaurant_erp.auth.service;

import com.devmasters.restaurant_erp.auth.model.*;
import com.devmasters.restaurant_erp.auth.domain.RefreshToken;
import com.devmasters.restaurant_erp.auth.domain.User;
import com.devmasters.restaurant_erp.auth.repository.RefreshTokenRepository;
import com.devmasters.restaurant_erp.auth.repository.UserRepository;
import com.devmasters.restaurant_erp.config.JwtTokenProvider;
import com.devmasters.restaurant_erp.auth.transformer.UserTransformer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public AuthResponse signup(SignupRequest request) {

        AuthResponse validationResponse = validateSignupRequest(request);
        if (validationResponse != null) return validationResponse;

        User user = createUser(request);
        User savedUser = userRepository.save(user);

        String accessToken = generateAccessToken(savedUser);
        String refreshToken = generateAndStoreRefreshToken(savedUser);

        return buildSuccessResponse(savedUser, accessToken, refreshToken);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = getUserByEmail(request.getEmail());

        if (!isValidUser(user, request.getPassword())) {
            return buildFailureResponse();
        }

        invalidateOldRefreshTokens(user);
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        saveRefreshToken(user, refreshToken);

        return buildSuccessResponse(user, accessToken, refreshToken);
    }



    @Override
    public AuthResponse changePassword(String email, ChangePasswordRequest request) {

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
    public UserProfileResponse getCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserTransformer.toProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(
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
    public AuthResponse refreshToken(
            RefreshTokenRequest request
    ) {

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByToken(request.getRefreshToken())
                        .orElse(null);

        if (refreshToken == null) {

            return AuthResponse.builder()
                    .success(false)
                    .message("Invalid refresh token")
                    .build();
        }

        if (refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            return AuthResponse.builder()
                    .success(false)
                    .message("Refresh token expired")
                    .build();
        }

        User user = userRepository.findById(
                refreshToken.getUserId()
        ).orElseThrow();

        String newAccessToken =
                jwtTokenProvider.generateAccessToken(user.getEmail(), user.getRole(), user.getTokenVersion());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .success(true)
                .message("Token refreshed")
                .build();
    }

    private AuthResponse validateSignupRequest(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Email already registered")
                    .build();
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Passwords do not match")
                    .build();
        }

        return null;
    }

    private User createUser(SignupRequest request) {
        return UserTransformer.toEntity(
                request,
                passwordEncoder.encode(request.getPassword())
        );
    }

    private String generateAccessToken(User user) {
        return jwtTokenProvider.generateAccessToken(
                user.getEmail(),
                user.getRole(),
                user.getTokenVersion()
        );
    }

    private String generateAndStoreRefreshToken(User user) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .token(refreshToken)
                        .userId(user.getId())
                        .expiryDate(LocalDateTime.now().plusDays(7))
                        .build()
        );

        return refreshToken;
    }






    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private boolean isValidUser(User user, String rawPassword) {
        return user != null &&
                passwordEncoder.matches(rawPassword, user.getPassword());
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
                        .token(refreshToken)
                        .userId(user.getId())
                        .expiryDate(LocalDateTime.now().plusDays(7))
                        .build()
        );
    }

    private AuthResponse buildFailureResponse() {
        return AuthResponse.builder()
                .success(false)
                .message("Invalid email or password")
                .build();
    }

    private AuthResponse buildSuccessResponse(User user, String accessToken, String refreshToken) {
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

    private AuthResponse success(String message) {
        return AuthResponse.builder()
                .success(true)
                .message(message)
                .build();
    }

    private AuthResponse error(String message) {
        return AuthResponse.builder()
                .success(false)
                .message(message)
                .build();
    }
}
