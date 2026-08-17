package com.devmasters.restaurant_erp.order.model.searchCriteria;

import com.devmasters.restaurant_erp.common.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDiscountSearchCriteria {

    private String searchInput;
    private DiscountType discountType;
    private UUID orderId;
    private UUID organizationId;
    private UUID branchId;
    private UUID appliedById;
    private Boolean isActive;
}