package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.OrderSource;
import com.devmasters.restaurant_erp.enums.OrderStatus;
import com.devmasters.restaurant_erp.enums.OrderType;
import com.devmasters.restaurant_erp.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchCriteria {

    private String searchInput;

    private OrderStatus status;

    private OrderType orderType;

    private OrderSource orderSource;

    private PaymentStatus paymentStatus;

    private UUID organizationId;

    private UUID branchId;

    private UUID customerId;

    private UUID restaurantTableId;

    private UUID deliveryPartnerId;
}