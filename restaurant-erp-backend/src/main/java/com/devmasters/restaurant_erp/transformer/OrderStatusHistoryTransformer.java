package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderStatusHistory;
import com.devmasters.restaurant_erp.model.order.OrderStatusHistoryModel;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusHistoryTransformer {

    public OrderStatusHistoryModel toModel(
            OrderStatusHistory statusHistory) {

        if (statusHistory == null) {
            return null;
        }

        return OrderStatusHistoryModel.builder()
                .id(statusHistory.getId())
                .previousStatus(statusHistory.getPreviousStatus())
                .newStatus(statusHistory.getNewStatus())
                .reason(statusHistory.getReason())
                .changedAt(statusHistory.getChangedAt())
                .build();
    }

    public OrderStatusHistory toEntity(
            OrderStatusHistoryModel model) {

        if (model == null) {
            return null;
        }

        return OrderStatusHistory.builder()
                .id(model.getId())
                .previousStatus(model.getPreviousStatus())
                .newStatus(model.getNewStatus())
                .reason(model.getReason())
                .changedAt(model.getChangedAt())
                .build();
    }
}