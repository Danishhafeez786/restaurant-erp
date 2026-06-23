package com.devmasters.restaurant_erp.domain.subclass;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItem {

    /**
     * Reference Information
     */
    private String menuItemId;

    private String itemCode;

    private String itemName;

    /**
     * Pricing
     */
    private Double unitPrice;

    private Integer quantity;

    private Double discountAmount = 0.0;

    private Double totalPrice;

    /**
     * Kitchen Information
     */
    private String kitchenStationId;

    private String kitchenStationName;

    /**
     * Special Instructions
     */
    private String notes;

    private Double costPrice;

    private Double profitAmount;

    private Boolean prepared;

    private LocalDateTime preparedAt;
}
