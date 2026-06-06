package com.devmasters.restaurant_erp.common.util;

import java.math.BigDecimal;

public class PriceCalculatorUtil {

    public static BigDecimal calculateTotal(BigDecimal price, int qty) {
        return price.multiply(BigDecimal.valueOf(qty));
    }

    public static BigDecimal applyDiscount(BigDecimal amount, double percent) {
        return amount.subtract(amount.multiply(BigDecimal.valueOf(percent / 100)));
    }

    public static BigDecimal addTax(BigDecimal amount, double taxPercent) {
        return amount.add(amount.multiply(BigDecimal.valueOf(taxPercent / 100)));
    }
}
