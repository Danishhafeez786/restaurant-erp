package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
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
    private String fullName;
    @NotBlank
    private String phone;
    private String referredBy;
    @NotBlank
    private String password;
    private RoleModel role;
    private String referralCode;
    private String accessToken;
    private String refreshToken;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private int tokenVersion;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
