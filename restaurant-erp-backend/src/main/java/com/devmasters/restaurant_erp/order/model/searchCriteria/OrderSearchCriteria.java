package com.devmasters.restaurant_erp.order.model.searchCriteria;

import com.devmasters.restaurant_erp.common.enums.OrderSource;
import com.devmasters.restaurant_erp.common.enums.OrderStatus;
import com.devmasters.restaurant_erp.common.enums.OrderType;
import com.devmasters.restaurant_erp.common.enums.PaymentStatus;
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