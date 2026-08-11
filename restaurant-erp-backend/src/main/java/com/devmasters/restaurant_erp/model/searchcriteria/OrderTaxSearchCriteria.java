package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.OrderTaxType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTaxSearchCriteria {

    private String searchInput;

    private OrderTaxType taxType;

    private UUID orderId;

    private UUID organizationId;

    private UUID branchId;
}