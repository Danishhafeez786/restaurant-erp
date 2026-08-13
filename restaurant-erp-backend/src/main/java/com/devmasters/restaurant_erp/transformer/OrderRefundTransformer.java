package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderRefund;
import com.devmasters.restaurant_erp.model.order.OrderRefundModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRefundTransformer {

    private final OrderTransformer orderTransformer;
    private final OrderPaymentTransformer orderPaymentTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;
    private final EmployeeTransformer employeeTransformer;

    public OrderRefund toEntity(OrderRefundModel model) {
        if (model == null) return null;

        return OrderRefund.builder()
                .id(model.getId())
                .refundNumber(model.getRefundNumber())
                .refundAmount(model.getRefundAmount())
                .status(model.getStatus())
                .reason(model.getReason())
                .note(model.getNote())
                .requestedAt(model.getRequestedAt())
                .processedAt(model.getProcessedAt())
                .transactionReference(model.getTransactionReference())
                .order(orderTransformer.toEntity(model.getOrderModel()))
                .orderPayment(orderPaymentTransformer.toEntity(model.getOrderPaymentModel()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .processedBy(employeeTransformer.toEntity(model.getProcessedByModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    public OrderRefundModel toModel(OrderRefund entity) {
        if (entity == null) return null;

        return OrderRefundModel.builder()
                .id(entity.getId())
                .refundNumber(entity.getRefundNumber())
                .refundAmount(entity.getRefundAmount())
                .status(entity.getStatus())
                .reason(entity.getReason())
                .note(entity.getNote())
                .requestedAt(entity.getRequestedAt())
                .processedAt(entity.getProcessedAt())
                .transactionReference(entity.getTransactionReference())
                .orderModel(orderTransformer.toModel(entity.getOrder()))
                .orderPaymentModel(orderPaymentTransformer.toModel(entity.getOrderPayment()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .processedByModel(employeeTransformer.toModel(entity.getProcessedBy()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public List<OrderRefundModel> toModels(List<OrderRefund> entities) {
        return entities == null ? null :
                entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}