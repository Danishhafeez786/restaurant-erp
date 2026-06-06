package com.devmasters.restaurant_erp.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtil {

    public static BigDecimal toBigDecimal(double value) {
        return BigDecimal.valueOf(value);
    }

    public static BigDecimal round(BigDecimal value, int places) {
        if (value == null) return BigDecimal.ZERO;

        return value.setScale(places, RoundingMode.HALF_UP);
    }

    public static BigDecimal percentage(BigDecimal amount, double percent) {
        if (amount == null) return BigDecimal.ZERO;

        return amount.multiply(BigDecimal.valueOf(percent / 100));
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return safe(a).add(safe(b));
    }

    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return safe(a).subtract(safe(b));
    }

    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return safe(a).multiply(safe(b));
    }

    public static BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
