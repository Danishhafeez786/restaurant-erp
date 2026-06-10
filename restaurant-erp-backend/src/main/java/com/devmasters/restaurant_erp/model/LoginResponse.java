package com.devmasters.restaurant_erp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String referralCode;
    private String accessToken;
    private String refreshToken;
    private String message;
    private boolean success;
}
