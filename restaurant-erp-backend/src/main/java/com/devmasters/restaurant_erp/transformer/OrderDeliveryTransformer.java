package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderDelivery;
import com.devmasters.restaurant_erp.model.order.OrderDeliveryModel;
import org.springframework.stereotype.Component;

@Component
public class OrderDeliveryTransformer {

    public OrderDeliveryModel toModel(OrderDelivery delivery) {

        if (delivery == null) {
            return null;
        }

        return OrderDeliveryModel.builder()
                .id(delivery.getId())
                .status(delivery.getStatus())
                .deliveryPartnerId(delivery.getDeliveryPartnerId())
                .deliveryAddress(delivery.getDeliveryAddress())
                .deliveryInstructions(delivery.getDeliveryInstructions())
                .assignedAt(delivery.getAssignedAt())
                .pickedUpAt(delivery.getPickedUpAt())
                .deliveredAt(delivery.getDeliveredAt())
                .cancellationReason(delivery.getCancellationReason())
                .build();
    }

    public OrderDelivery toEntity(OrderDeliveryModel model) {

        if (model == null) {
            return null;
        }

        return OrderDelivery.builder()
                .id(model.getId())
                .status(model.getStatus())
                .deliveryPartnerId(model.getDeliveryPartnerId())
                .deliveryAddress(model.getDeliveryAddress())
                .deliveryInstructions(model.getDeliveryInstructions())
                .assignedAt(model.getAssignedAt())
                .pickedUpAt(model.getPickedUpAt())
                .deliveredAt(model.getDeliveredAt())
                .cancellationReason(model.getCancellationReason())
                .build();
    }
}