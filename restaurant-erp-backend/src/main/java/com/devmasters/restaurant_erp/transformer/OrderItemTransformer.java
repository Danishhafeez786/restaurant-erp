package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderItem;
import com.devmasters.restaurant_erp.model.order.OrderItemModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderItemTransformer {

    private final OrderItemModifierTransformer orderItemModifierTransformer;

    public OrderItemModel toModel(OrderItem entity) {
        if (entity == null)
            return null;

        return OrderItemModel.builder()
                .id(entity.getId())
                .menuItemId(entity.getMenuItemId())
                .itemName(entity.getItemName())
                .itemCode(entity.getItemCode())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .grossAmount(entity.getGrossAmount())
                .discountAmount(entity.getDiscountAmount())
                .taxAmount(entity.getTaxAmount())
                .totalAmount(entity.getTotalAmount())
                .specialInstructions(entity.getSpecialInstructions())
                .modifiers(entity.getModifiers() == null ? null :
                        entity.getModifiers().stream()
                        .map(orderItemModifierTransformer::toModel)
                        .collect(Collectors.toList()))
                .build();
    }

    public OrderItem toEntity(OrderItemModel model) {
        if (model == null)
            return null;

        return OrderItem.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .menuItemId(model.getMenuItemId())
                .itemName(model.getItemName())
                .itemCode(model.getItemCode())
                .quantity(model.getQuantity())
                .unitPrice(model.getUnitPrice())
                .grossAmount(model.getGrossAmount())
                .discountAmount(model.getDiscountAmount())
                .taxAmount(model.getTaxAmount())
                .totalAmount(model.getTotalAmount())
                .specialInstructions(model.getSpecialInstructions())
                .modifiers(model.getModifiers() == null ? null :
                        model.getModifiers().stream()
                        .map(orderItemModifierTransformer::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    public List<OrderItemModel> toModels(List<OrderItem> entities) {
        if (entities == null)
            return null;

        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<OrderItem> toEntities(List<OrderItemModel> models) {
        if (models == null)
            return null;

        return models.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}