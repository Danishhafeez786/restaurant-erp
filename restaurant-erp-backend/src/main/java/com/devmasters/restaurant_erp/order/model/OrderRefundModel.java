package com.devmasters.restaurant_erp.order.model;


import com.devmasters.restaurant_erp.paymentmethod.domain.PaymentMethod;
import com.devmasters.restaurant_erp.common.enums.RefundReason;
import com.devmasters.restaurant_erp.common.enums.RefundStatus;
import com.devmasters.restaurant_erp.branch.model.BranchModel;
import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
import com.devmasters.restaurant_erp.employee.model.EmployeeModel;
import lombok.*;

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
    private BigDecimal refundAmount;
    private PaymentMethod paymentMethod;
    private RefundStatus status;
    private RefundReason reason;
    private String note;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private String transactionReference;
    private OrderModel orderModel;
    private OrderPaymentModel orderPaymentModel;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private EmployeeModel processedByModel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}