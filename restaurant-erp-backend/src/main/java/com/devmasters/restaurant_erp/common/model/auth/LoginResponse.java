package com.devmasters.restaurant_erp.common.model.auth;

import com.devmasters.restaurant_erp.common.enums.RoleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;

    private String userId;

    private RoleType role;

    private String tenantId;
}
