package com.devmasters.restaurant_erp.common.constants;

public final class AppConstants {

    private AppConstants() {}

    // App Info
    public static final String APP_NAME = "Restaurant POS ERP";
    public static final String APP_VERSION = "1.0.0";

    // Default values
    public static final String DEFAULT_CURRENCY = "PKR";
    public static final String DEFAULT_TIMEZONE = "Asia/Karachi";

    // Pagination
    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 20;

    // Order
    public static final String ORDER_PREFIX = "ORD";
    public static final String BILL_PREFIX = "BILL";

    // Kitchen
    public static final int DEFAULT_KITCHEN_TIMEOUT_MINUTES = 20;

    // Inventory
    public static final double LOW_STOCK_THRESHOLD = 10.0;

    // Tax
    public static final double DEFAULT_TAX_PERCENT = 0.0;

    // Status Messages
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    // Messages
    public static final String CREATED = "Created successfully";
    public static final String UPDATED = "Updated successfully";
    public static final String DELETED = "Deleted successfully";

    // Error Messages
    public static final String NOT_FOUND = "Resource not found";
    public static final String UNAUTHORIZED = "Unauthorized access";
    public static final String FORBIDDEN = "Access denied";
}
