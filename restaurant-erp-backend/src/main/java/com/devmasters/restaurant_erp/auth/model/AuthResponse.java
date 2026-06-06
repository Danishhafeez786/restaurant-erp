package com.devmasters.restaurant_erp.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;

    private String email;
    private String firstName;
    private String lastName;

    private String role;

    private String referralCode;

    private String message;
    private boolean success;
}
