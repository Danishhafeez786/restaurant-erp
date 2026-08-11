package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.enums.DeliveryStatus;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.order.OrderModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeliveryModel {

    private UUID id;

    private DeliveryStatus status;

    private String deliveryAddress;

    private String deliveryInstructions;


    private LocalDateTime assignedAt;

    private LocalDateTime pickedUpAt;

    private LocalDateTime deliveredAt;


    private String cancellationReason;


    private OrderModel order;

    private UUID deliveryPartnerId;

    private OrganizationModel organization;

    private BranchModel branch;
}
