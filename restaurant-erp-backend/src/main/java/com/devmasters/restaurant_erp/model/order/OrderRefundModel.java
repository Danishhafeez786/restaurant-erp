package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.enums.OrderPaymentMethod;
import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRefundModel {

    private UUID id;

    private String refundNumber;

    private BigDecimal amount;

    private OrderPaymentMethod paymentMethod;

    private String reason;

    private LocalDateTime refundedAt;


    private OrderModel order;

    private UserModel refundedBy;

    private OrganizationModel organization;

    private BranchModel branch;
}


