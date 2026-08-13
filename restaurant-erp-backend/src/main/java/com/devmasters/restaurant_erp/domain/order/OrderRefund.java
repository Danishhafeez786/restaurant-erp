package com.devmasters.restaurant_erp.domain.order;

import com.devmasters.restaurant_erp.domain.*;
import com.devmasters.restaurant_erp.enums.RefundReason;
import com.devmasters.restaurant_erp.enums.RefundStatus;
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