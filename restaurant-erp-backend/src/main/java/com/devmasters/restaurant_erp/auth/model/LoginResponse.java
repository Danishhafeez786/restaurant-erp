package com.devmasters.restaurant_erp.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String id;
    private String fullName;
    private String email;

    private String roleId;
    private String role;

    private String referralCode;

    private String accessToken;
    private String refreshToken;

    private List<String> permissions;

    private String message;
    private boolean success;
}
