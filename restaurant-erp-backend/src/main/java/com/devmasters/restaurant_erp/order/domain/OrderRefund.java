package com.devmasters.restaurant_erp.order.domain;

import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.common.enums.RefundReason;
import com.devmasters.restaurant_erp.common.enums.RefundStatus;
import com.devmasters.restaurant_erp.employee.domain.Employee;
import com.devmasters.restaurant_erp.organization.domain.Organization;
import com.devmasters.restaurant_erp.paymentmethod.domain.PaymentMethod;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("order_refunds")
public class OrderRefund extends BaseEntity {

    private String refundNumber;
    private BigDecimal refundAmount;
    private PaymentMethod paymentMethod;
    private RefundStatus status;
    private RefundReason reason;
    private String note;
    private LocalDateTime requestedAt;
    private LocalDateTime processedAt;
    private String transactionReference;

    @DBRef
    private Order order;

    @DBRef
    private OrderPayment orderPayment;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;

    @DBRef
    private Employee processedBy;
}