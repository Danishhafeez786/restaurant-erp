package com.devmasters.restaurant_erp.order.model.searchCriteria;

import com.devmasters.restaurant_erp.common.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliverySearchCriteria {

    private UUID orderId;
    private DeliveryStatus status;
    private String deliveryPartnerId;
    private UUID organizationId;
    private UUID branchId;
    private UUID assignedById;
    private Boolean isActive;
}