package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.enums.KitchenTicketStatus;
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
public class OrderKitchenTicketModel {

    private UUID id;

    private String ticketNumber;

    private KitchenTicketStatus status;

    private String kitchenStation;


    private LocalDateTime sentAt;

    private LocalDateTime startedAt;

    private LocalDateTime readyAt;


    private String note;


    private OrderModel order;

    private OrganizationModel organization;

    private BranchModel branch;
}

