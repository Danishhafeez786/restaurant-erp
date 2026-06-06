package com.devmasters.restaurant_erp.common.model.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenValidationResponse {

    private boolean valid;

    private String userId;

    private String role;

    private String tenantId;
}
