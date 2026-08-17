package com.devmasters.restaurant_erp.order.domain;

import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.common.enums.PaymentStatus;
import com.devmasters.restaurant_erp.employee.domain.Employee;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.paymentmethod.domain.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("order_payments")
public class OrderPayment extends BaseEntity {

    private String paymentNumber;

    private UUID orderId;

    @DBRef
    private PaymentMethod paymentMethod;

    private PaymentStatus status;

    private BigDecimal amount;

    private String transactionReference;

    private String paymentNote;

    private LocalDateTime paidAt;

    private LocalDateTime refundedAt;

    private String refundReason;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;

    @DBRef
    private Employee receivedBy;

    @DBRef
    private Employee refundedBy;
}