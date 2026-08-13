package com.devmasters.restaurant_erp.domain.order;

import com.devmasters.restaurant_erp.domain.*;
import com.devmasters.restaurant_erp.enums.DiscountType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("order_discounts")
public class OrderDiscount extends BaseEntity {

    private String discountNumber;
    private String discountName;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal discountAmount;
    private BigDecimal taxableAmount;

    @DBRef
    private Order order;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;

    @DBRef
    private Employee appliedBy;
}