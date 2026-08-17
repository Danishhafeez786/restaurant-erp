package com.devmasters.restaurant_erp.order.model.searchCriteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTaxSearchCriteria {

    private String searchInput;
    private UUID orderId;
    private UUID taxId;
    private UUID organizationId;
    private UUID branchId;
    private UUID appliedById;
    private Boolean isActive;
}