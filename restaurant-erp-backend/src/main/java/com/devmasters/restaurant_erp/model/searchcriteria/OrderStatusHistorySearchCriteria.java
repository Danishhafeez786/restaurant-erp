package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusHistorySearchCriteria {

    private UUID orderId;
    private OrderStatus previousStatus;
    private OrderStatus newStatus;
    private UUID organizationId;
    private UUID branchId;
    private UUID changedById;
}