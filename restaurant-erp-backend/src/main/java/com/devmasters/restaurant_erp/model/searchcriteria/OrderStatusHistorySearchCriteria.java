package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusHistorySearchCriteria {

    private String searchInput;

    private OrderStatus previousStatus;

    private OrderStatus newStatus;

    private UUID orderId;

    private UUID organizationId;

    private UUID branchId;

    private LocalDateTime changedAtFrom;

    private LocalDateTime changedAtTo;
}