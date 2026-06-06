package com.devmasters.restaurant_erp.common.util;


import com.devmasters.restaurant_erp.common.enums.OrderStatus;

public class StatusTransitionUtil {

    public static boolean isValidTransition(OrderStatus from, OrderStatus to) {

        return switch (from) {
            case PLACED -> to == OrderStatus.IN_KITCHEN;
            case IN_KITCHEN -> to == OrderStatus.READY;
            case READY -> to == OrderStatus.DELIVERED;
            default -> false;
        };
    }
}