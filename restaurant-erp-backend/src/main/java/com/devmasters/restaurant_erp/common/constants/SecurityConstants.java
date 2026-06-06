package com.devmasters.restaurant_erp.common.constants;

public final class SecurityConstants {

    private SecurityConstants() {}

    // JWT
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    // Token claims
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_BRANCH = "branchId";
    public static final String CLAIM_PERMISSIONS = "permissions";

    // Roles
    public static final String ROLE_OWNER = "OWNER";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_CHEF = "CHEF";
    public static final String ROLE_CASHIER = "CASHIER";
    public static final String ROLE_WAITER = "WAITER";

    // Public URLs (no auth required)
    public static final String[] PUBLIC_URLS = {
            "/api/v1/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/ws/**"
    };

    // Token expiry (fallback if not using properties)
    public static final long ACCESS_TOKEN_EXPIRY = 1000 * 60 * 60 * 10; // 10 hours
}
