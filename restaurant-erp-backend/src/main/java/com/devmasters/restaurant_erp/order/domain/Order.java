package com.devmasters.restaurant_erp.order.domain;

import com.devmasters.restaurant_erp.auth.domain.User;
import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.customer.domain.Customer;
import com.devmasters.restaurant_erp.common.enums.OrderSource;
import com.devmasters.restaurant_erp.common.enums.OrderStatus;
import com.devmasters.restaurant_erp.common.enums.OrderType;
import com.devmasters.restaurant_erp.common.enums.PaymentStatus;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.tablemanagment.domain.RestaurantTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("orders")
public class  Order extends BaseEntity {

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


    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;

    @DBRef
    private Customer customer;

    @DBRef
    private RestaurantTable restaurantTable;

    @DBRef
    private User createdBy;

    @DBRef
    private User updatedBy;

    @DBRef
    private User cancelledBy;

    private List<OrderItem> items;
}