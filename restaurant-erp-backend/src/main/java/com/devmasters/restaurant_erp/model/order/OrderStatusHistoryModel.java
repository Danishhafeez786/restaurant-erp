package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.enums.OrderStatus;
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
public class OrderStatusHistoryModel {

    private UUID id;
    private OrderStatus previousStatus;
    private OrderStatus newStatus;
    private String reason;
    private LocalDateTime changedAt;
    private OrderModel orderModel;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private EmployeeModel changedByModel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}