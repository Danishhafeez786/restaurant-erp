package com.devmasters.restaurant_erp.order.domain;

import com.devmasters.restaurant_erp.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemModifier extends BaseEntity {

    private UUID modifierId;
    private String modifierName;
    private String modifierCode;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
}