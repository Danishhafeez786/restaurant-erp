package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.domain.order.DeliveryAddress;
import com.devmasters.restaurant_erp.enums.DeliveryStatus;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.UserModel;
import com.devmasters.restaurant_erp.model.employee.EmployeeModel;
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
    private DeliveryAddress deliveryAddress;
    private String deliveryInstructions;
    private String deliveryPartnerId;
    private DeliveryStatus status;
    private LocalDateTime assignedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private OrderModel orderModel;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private EmployeeModel assignedByModel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}