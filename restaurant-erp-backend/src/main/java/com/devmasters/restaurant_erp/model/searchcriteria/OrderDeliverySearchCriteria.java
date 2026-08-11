package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliverySearchCriteria {

    private String searchInput;

    private DeliveryStatus status;

    private UUID orderId;

    private UUID deliveryPartnerId;

    private UUID organizationId;

    private UUID branchId;

    private LocalDateTime assignedAtFrom;

    private LocalDateTime assignedAtTo;
}