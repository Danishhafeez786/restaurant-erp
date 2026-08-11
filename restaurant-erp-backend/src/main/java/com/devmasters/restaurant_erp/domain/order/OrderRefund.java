package com.devmasters.restaurant_erp.domain.order;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.domain.User;
import com.devmasters.restaurant_erp.enums.OrderPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

    private BigDecimal amount;

    private OrderPaymentMethod paymentMethod;

    private String reason;

    private LocalDateTime refundedAt;


    @DBRef
    private Order order;

    @DBRef
    private User refundedBy;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}