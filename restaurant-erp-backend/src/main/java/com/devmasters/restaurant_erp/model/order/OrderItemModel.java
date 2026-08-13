package com.devmasters.restaurant_erp.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemModel {

    private UUID id;

    private UUID menuItemId;

    private String itemName;

    private String itemCode;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal grossAmount;

    private BigDecimal discountAmount;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    private String specialInstructions;

    private List<OrderItemModifierModel> modifiers;
}