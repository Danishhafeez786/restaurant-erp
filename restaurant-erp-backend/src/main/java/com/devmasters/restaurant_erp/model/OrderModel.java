package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.domain.subclass.OrderItem;
import com.devmasters.restaurant_erp.enums.OrderStatus;
import com.devmasters.restaurant_erp.enums.OrderType;
import com.devmasters.restaurant_erp.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderModel {
    private UUID id;
    private String orderNumber;
    private OrderType orderType;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private Integer persons;
    private Double grossAmount;
    private Double discountAmount;
    private Double taxAmount;
    private Double netAmount;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private CustomerModel customerModel;
    private RestaurantTableModel restaurantTableModel;
    private UserModel createdBy;
    private List<OrderItem> items;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
