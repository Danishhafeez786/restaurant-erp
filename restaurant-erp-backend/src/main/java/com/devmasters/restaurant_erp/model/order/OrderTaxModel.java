package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.enums.OrderTaxType;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.order.OrderModel;
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
public class OrderTaxModel {

    private UUID id;

    private String taxCode;

    private String taxName;

    private OrderTaxType taxType;

    private BigDecimal taxValue;

    private BigDecimal taxAmount;


    private OrderModel order;

    private OrganizationModel organization;

    private BranchModel branch;
}

