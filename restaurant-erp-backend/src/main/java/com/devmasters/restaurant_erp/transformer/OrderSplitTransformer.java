package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderSplit;
import com.devmasters.restaurant_erp.model.order.OrderSplitModel;
import org.springframework.stereotype.Component;

@Component
public class OrderSplitTransformer {

    public OrderSplitModel toModel(
            OrderSplit split) {

        if (split == null) {
            return null;
        }

        return OrderSplitModel.builder()
                .id(split.getId())
                .splitNumber(split.getSplitNumber())
                .amount(split.getAmount())
                .paymentMethod(split.getPaymentMethod())
                .paid(split.getPaid())
                .build();
    }

    public OrderSplit toEntity(
            OrderSplitModel model) {

        if (model == null) {
            return null;
        }

        return OrderSplit.builder()
                .id(model.getId())
                .splitNumber(model.getSplitNumber())
                .amount(model.getAmount())
                .paymentMethod(model.getPaymentMethod())
                .paid(model.getPaid())
                .build();
    }
}