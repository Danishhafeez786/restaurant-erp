package com.devmasters.restaurant_erp.common.constants;

public final class ApiConstants {

    private ApiConstants() {}

    // Base API
    public static final String API_VERSION = "/api/v1";

    // Auth
    public static final String AUTH = API_VERSION + "/auth";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";

    // User
    public static final String USERS = API_VERSION + "/users";

    // Branch
    public static final String BRANCHES = API_VERSION + "/branches";

    // Role
    public static final String ROLES = API_VERSION + "/roles";

    // Menu
    public static final String MENU = API_VERSION + "/menu";

    // Order
    public static final String ORDERS = API_VERSION + "/orders";

    // Kitchen
    public static final String KITCHEN = API_VERSION + "/kitchen";

    // Inventory
    public static final String INVENTORY = API_VERSION + "/inventory";

    // Payment
    public static final String PAYMENTS = API_VERSION + "/payments";

    // Reports
    public static final String REPORTS = API_VERSION + "/reports";

    // Dashboard
    public static final String DASHBOARD = API_VERSION + "/dashboard";

    // WebSocket
    public static final String WS = "/ws";
}
