package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.KitchenOrder;
import com.devmasters.restaurant_erp.model.KitchenOrderModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class KitchenOrderTransformer extends Transformer<KitchenOrder, KitchenOrderModel>{
    private final OrderTransformer orderTransformer;

    @Override
    public KitchenOrder toEntity(KitchenOrderModel model) {
        if(model == null)
            return null;
        return KitchenOrder.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .order(orderTransformer.toEntity(model.getOrderModel()))
                .kitchenStatus(model.getKitchenStatus())
                .estimatedMinutes(model.getEstimatedMinutes())
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public KitchenOrderModel toModel(KitchenOrder entity) {
        if(entity == null)
            return null;
        return KitchenOrderModel.builder()
                .id(entity.getId())
                .orderModel(orderTransformer.toModel(entity.getOrder()))
                .kitchenStatus(entity.getKitchenStatus())
                .estimatedMinutes(entity.getEstimatedMinutes())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
