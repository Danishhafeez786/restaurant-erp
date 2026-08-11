package com.devmasters.restaurant_erp.domain.order;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.enums.OrderTaxType;
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
@Document("order_taxes")
public class OrderTax extends BaseEntity {

    private String taxCode;

    private String taxName;

    private OrderTaxType taxType;

    private BigDecimal taxValue;

    private BigDecimal taxAmount;


    @DBRef
    private Order order;

    @DBRef
    private Organization organization;

    @DBRef
    private Branch branch;
}