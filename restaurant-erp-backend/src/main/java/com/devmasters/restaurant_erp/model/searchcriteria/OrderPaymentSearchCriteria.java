package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.PaymentStatus;
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