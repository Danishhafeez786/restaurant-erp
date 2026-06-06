package com.devmasters.restaurant_erp.common.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtil {

    public static String format(BigDecimal amount) {
        if (amount == null) return "0.00";

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "PK"));
        return format.format(amount);
    }
}
