package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {

    private UUID id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    private String zip;

    private String referredBy;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;
    private Boolean isActive;

    private Role role;

    private String referralCode;
    private String accessToken;
    private String refreshToken;
}
