package com.devmasters.restaurant_erp.order.model.searchCriteria;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSplitSearchCriteria {
    private String keyword;
    private UUID orderId;
    private UUID organizationId;
    private UUID branchId;
    private Boolean paid;
    private BigDecimal minTotalAmount;
    private BigDecimal maxTotalAmount;
    private Boolean isActive;
}