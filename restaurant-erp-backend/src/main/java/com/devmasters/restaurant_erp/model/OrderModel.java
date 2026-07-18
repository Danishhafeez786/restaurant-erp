package com.devmasters.restaurant_erp.model;

import com.devmasters.restaurant_erp.domain.subclass.OrderItem;
import com.devmasters.restaurant_erp.enums.OrderStatus;
import com.devmasters.restaurant_erp.enums.OrderType;
import com.devmasters.restaurant_erp.enums.PaymentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Order number is required")
    @Size(max = 50, message = "Order number cannot exceed 50 characters")
    private String orderNumber;

    @NotNull(message = "Order type is required")
    private OrderType orderType;

    @NotNull(message = "Order status is required")
    private OrderStatus status;

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;

    @NotNull(message = "Number of persons is required")
    @Min(value = 1, message = "Persons must be at least 1")
    private Integer persons;

    @NotNull(message = "Gross amount is required")
    @PositiveOrZero(message = "Gross amount cannot be negative")
    private Double grossAmount;

    @NotNull(message = "Discount amount is required")
    @PositiveOrZero(message = "Discount amount cannot be negative")
    private Double discountAmount;

    @NotNull(message = "Tax amount is required")
    @PositiveOrZero(message = "Tax amount cannot be negative")
    private Double taxAmount;

    @NotNull(message = "Net amount is required")
    @PositiveOrZero(message = "Net amount cannot be negative")
    private Double netAmount;

    @Valid
    @NotNull(message = "Organization is required")
    private OrganizationModel organizationModel;

    @Valid
    @NotNull(message = "Branch is required")
    private BranchModel branchModel;

    @Valid
    private CustomerModel customerModel;

    @Valid
    private RestaurantTableModel restaurantTableModel;

    @Valid
    @NotNull(message = "Created by user is required")
    private UserModel createdBy;

    @Valid
    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderItem> items;

    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
