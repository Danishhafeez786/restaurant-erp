package com.devmasters.restaurant_erp.auth.domain;

import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.role.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("users")
public class User extends BaseEntity {
    private String username;
    private String password;
    private String fullName;
    @Indexed(unique = true)
    private String email;
    private String phone;
    @DBRef
    private Organization organization;
    @DBRef
    private Branch branch;
    @DBRef
    private Role role;
    private String referralCode;
    private String accessToken;
    private String refreshToken;
    private int tokenVersion = 0;
}
