package com.devmasters.restaurant_erp.order.model;

import com.devmasters.restaurant_erp.common.enums.OrderSource;
import com.devmasters.restaurant_erp.common.enums.OrderStatus;
import com.devmasters.restaurant_erp.common.enums.OrderType;
import com.devmasters.restaurant_erp.common.enums.PaymentStatus;
import com.devmasters.restaurant_erp.branch.model.BranchModel;
import com.devmasters.restaurant_erp.customer.model.CustomerModel;
import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
import com.devmasters.restaurant_erp.tablemanagment.model.RestaurantTableModel;
import com.devmasters.restaurant_erp.auth.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {

    private UUID id;

    private String orderNumber;

    private OrderType orderType;

    private OrderSource orderSource;

    private OrderStatus status;

    private PaymentStatus paymentStatus;

    private Integer persons;

    private BigDecimal subtotalAmount;

    private BigDecimal discountAmount;

    private BigDecimal taxAmount;

    private BigDecimal serviceChargeAmount;

    private BigDecimal deliveryChargeAmount;

    private BigDecimal roundingAmount;

    private BigDecimal totalAmount;

    private LocalDateTime orderedAt;

    private LocalDateTime confirmedAt;

    private LocalDateTime preparingAt;

    private LocalDateTime readyAt;

    private LocalDateTime completedAt;

    private LocalDateTime cancelledAt;

    private String customerNote;

    private String internalNote;

    private String cancellationReason;

    private String tableSessionNumber;

    private OrganizationModel organizationModel;

    private BranchModel branchModel;

    private CustomerModel customerModel;

    private RestaurantTableModel restaurantTableModel;

    private UserModel createdByModel;

    private UserModel updatedByModel;

    private UserModel cancelledByModel;

    private List<OrderItemModel> items;
}