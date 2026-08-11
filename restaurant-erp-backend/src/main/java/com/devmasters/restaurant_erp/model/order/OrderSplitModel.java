package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.enums.OrderPaymentMethod;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSplitModel {

    private UUID id;

    private Integer splitNumber;

    private BigDecimal amount;

    private OrderPaymentMethod paymentMethod;

    private Boolean paid;


    private OrderModel order;

    private OrganizationModel organization;

    private BranchModel branch;
}

