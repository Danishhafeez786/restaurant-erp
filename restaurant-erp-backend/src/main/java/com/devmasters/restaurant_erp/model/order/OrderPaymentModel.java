package com.devmasters.restaurant_erp.model.order;

import com.devmasters.restaurant_erp.model.BranchModel;
import com.devmasters.restaurant_erp.model.OrganizationModel;
import com.devmasters.restaurant_erp.model.PaymentMethodModel;
import com.devmasters.restaurant_erp.model.UserModel;
import com.devmasters.restaurant_erp.enums.PaymentStatus;
import com.devmasters.restaurant_erp.model.employee.EmployeeModel;
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