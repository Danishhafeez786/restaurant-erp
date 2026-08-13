package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.domain.order.DeliveryAddress;
import com.devmasters.restaurant_erp.domain.order.OrderItem;
import com.devmasters.restaurant_erp.enums.OrderSource;
import com.devmasters.restaurant_erp.enums.OrderStatus;
import com.devmasters.restaurant_erp.enums.OrderType;
import com.devmasters.restaurant_erp.enums.PaymentStatus;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.CustomerModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.RestaurantTableModel;
import com.devmasters.restaurant_erp.model.UserModel;
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