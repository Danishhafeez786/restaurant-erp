package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.KitchenTicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderKitchenTicketSearchCriteria {

    private String ticketNumber;

    private UUID orderId;

    private KitchenTicketStatus status;

    private Integer priority;

    private UUID organizationId;

    private UUID branchId;

    private UUID assignedToId;

    private Boolean isActive;
}