package com.devmasters.restaurant_erp.domain.order;

import com.devmasters.restaurant_erp.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {

    private UUID menuItemId;

    private String itemName;

    private String itemCode;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal grossAmount;

    private BigDecimal discountAmount;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    private String specialInstructions;

    private List<OrderItemModifier> modifiers;
}