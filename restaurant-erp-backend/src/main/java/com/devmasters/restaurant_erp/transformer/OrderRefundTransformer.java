package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderRefund;
import com.devmasters.restaurant_erp.model.order.OrderRefundModel;
import org.springframework.stereotype.Component;

@Component
public class OrderRefundTransformer {

    public OrderRefundModel toModel(
            OrderRefund refund) {

        if (refund == null) {
            return null;
        }

        return OrderRefundModel.builder()
                .id(refund.getId())
                .refundNumber(refund.getRefundNumber())
                .amount(refund.getAmount())
                .paymentMethod(refund.getPaymentMethod())
                .reason(refund.getReason())
                .refundedAt(refund.getRefundedAt())
                .build();
    }

    public OrderRefund toEntity(
            OrderRefundModel model) {

        if (model == null) {
            return null;
        }

        return OrderRefund.builder()
                .id(model.getId())
                .refundNumber(model.getRefundNumber())
                .amount(model.getAmount())
                .paymentMethod(model.getPaymentMethod())
                .reason(model.getReason())
                .refundedAt(model.getRefundedAt())
                .build();
    }
}