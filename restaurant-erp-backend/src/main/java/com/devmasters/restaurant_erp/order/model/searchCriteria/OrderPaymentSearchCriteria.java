package com.devmasters.restaurant_erp.order.model.searchCriteria;

import com.devmasters.restaurant_erp.common.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentSearchCriteria {

    private String searchInput;

    private PaymentStatus status;

    private UUID orderId;

    private UUID organizationId;

    private UUID branchId;

    private UUID paymentMethodId;

    private UUID receivedById;
}