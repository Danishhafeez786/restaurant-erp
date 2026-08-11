package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderItemModifier;
import com.devmasters.restaurant_erp.model.order.OrderItemModifierModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class OrderItemModifierTransformer {

    public OrderItemModifierModel toModel(OrderItemModifier entity) {
        if (entity == null) {
            return null;
        }

        return OrderItemModifierModel.builder()
                .id(entity.getId())
                .modifierId(entity.getModifierId())
                .modifierName(entity.getModifierName())
                .modifierCode(entity.getModifierCode())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .totalAmount(entity.getTotalAmount())
                .build();
    }

    public OrderItemModifier toEntity(OrderItemModifierModel model) {
        if (model == null) {
            return null;
        }

        return OrderItemModifier.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .modifierId(model.getModifierId())
                .modifierName(model.getModifierName())
                .modifierCode(model.getModifierCode())
                .quantity(model.getQuantity())
                .unitPrice(model.getUnitPrice())
                .totalAmount(model.getTotalAmount())
                .build();
    }
}