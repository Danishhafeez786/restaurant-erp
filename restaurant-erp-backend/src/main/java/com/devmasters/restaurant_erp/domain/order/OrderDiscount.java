package com.devmasters.restaurant_erp.domain.order;

import com.devmasters.restaurant_erp.domain.*;
import com.devmasters.restaurant_erp.enums.OrderDiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("order_discounts")
public class OrderDiscount extends BaseEntity {

    private String discountNumber;
    private UUID orderId;
    private String discountName;
    private OrderDiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal discountAmount;
    private String reason;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;

    @DBRef
    private Employee appliedBy;
}