package com.devmasters.restaurant_erp.order.model;

import com.devmasters.restaurant_erp.branch.model.BranchModel;
import com.devmasters.restaurant_erp.organization.model.OrganizationModel;
import com.devmasters.restaurant_erp.paymentmethod.model.PaymentMethodModel;
import com.devmasters.restaurant_erp.common.enums.PaymentStatus;
import com.devmasters.restaurant_erp.employee.model.EmployeeModel;
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
public class OrderPaymentModel {

    private UUID id;
    private String paymentNumber;
    private UUID orderId;
    private PaymentMethodModel paymentMethodModel;
    private PaymentStatus status;
    private BigDecimal amount;
    private String transactionReference;
    private String paymentNote;
    private LocalDateTime paidAt;
    private LocalDateTime refundedAt;
    private String refundReason;
    private OrganizationModel organizationModel;
    private BranchModel branchModel;
    private EmployeeModel receivedByModel;
    private EmployeeModel refundedByModel;
}