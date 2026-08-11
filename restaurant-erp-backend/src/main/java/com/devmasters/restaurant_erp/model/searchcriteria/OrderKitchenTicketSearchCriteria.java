package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.KitchenTicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderKitchenTicketSearchCriteria {

    private String searchInput;

    private KitchenTicketStatus status;

    private String kitchenStation;

    private UUID orderId;

    private UUID organizationId;

    private UUID branchId;

    private LocalDateTime sentAtFrom;

    private LocalDateTime sentAtTo;
}