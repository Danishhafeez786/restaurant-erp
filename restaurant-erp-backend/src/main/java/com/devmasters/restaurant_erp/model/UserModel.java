package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.enums.Role;
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
    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    private String address;
    private String city;
    private String state;
    private String zip;
    private Role role;

    private String referralCode;
    private String accessToken;
    private String refreshToken;


}
