package com.devmasters.restaurant_erp.domain.order;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.enums.OrderPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document("order_splits")
public class OrderSplit extends BaseEntity {

    private Integer splitNumber;

    private BigDecimal amount;

    private OrderPaymentMethod paymentMethod;

    private Boolean paid;


    @DBRef
    private Order order;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}