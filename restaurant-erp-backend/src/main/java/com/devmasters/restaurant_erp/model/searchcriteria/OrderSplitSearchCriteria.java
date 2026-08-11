package com.devmasters.restaurant_erp.model.searchcriteria;

import com.devmasters.restaurant_erp.enums.OrderPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSplitSearchCriteria {

    private String searchInput;

    private OrderPaymentMethod paymentMethod;

    private Boolean paid;

    private UUID orderId;

    private UUID organizationId;

    private UUID branchId;
}