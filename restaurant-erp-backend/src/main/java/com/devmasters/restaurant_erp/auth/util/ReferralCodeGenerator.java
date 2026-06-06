package com.devmasters.restaurant_erp.auth.util;

import java.util.UUID;

public class ReferralCodeGenerator {

    public static String generate() {
        return "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}