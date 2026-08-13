package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.enums.KitchenTicketStatus;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
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
public class OrderKitchenTicketModel {

    private UUID id;

    private String ticketNumber;

    private KitchenTicketStatus status;

    private Integer priority;

    private String kitchenNote;

    private LocalDateTime sentAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime preparingAt;

    private LocalDateTime readyAt;

    private LocalDateTime completedAt;

    private LocalDateTime cancelledAt;

    private String cancellationReason;

    private OrderModel orderModel;

    private OrganizationModel organizationModel;

    private BranchModel branchModel;

    private EmployeeModel assignedToModel;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}