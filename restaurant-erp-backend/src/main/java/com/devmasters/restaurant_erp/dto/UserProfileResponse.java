package com.devmasters.restaurant_erp.dto;

import com.devmasters.restaurant_erp.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    private String address;
    private String city;
    private String state;
    private String zip;

    private String referralCode;

    private Role role;
}
