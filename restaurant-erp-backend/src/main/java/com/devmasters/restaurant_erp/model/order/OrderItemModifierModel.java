package com.devmasters.restaurant_erp.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemModifierModel {

    private UUID id;
    private UUID modifierId;
    private String modifierName;
    private String modifierCode;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
}