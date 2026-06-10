package com.devmasters.restaurant_erp.domain;

import com.devmasters.restaurant_erp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private UUID id;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String firstName;
    private String lastName;

    private String phone;

    private String address;
    private String city;
    private String state;
    private String zip;

    @Indexed(unique = true)
    private String referralCode;

    private String referredBy;

    private Role role;

    private boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int tokenVersion = 0;

}