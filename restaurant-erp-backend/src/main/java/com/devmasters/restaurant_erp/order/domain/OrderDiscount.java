package com.devmasters.restaurant_erp.order.domain;

import com.devmasters.restaurant_erp.branch.domain.Branch;
import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import com.devmasters.restaurant_erp.common.enums.DiscountType;
import com.devmasters.restaurant_erp.employee.domain.Employee;
import com.devmasters.restaurant_erp.organization.domain.Organization;
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