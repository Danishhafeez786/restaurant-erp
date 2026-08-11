package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.order.OrderPayment;
import com.devmasters.restaurant_erp.model.order.OrderPaymentModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class OrderPaymentTransformer extends Transformer<OrderPayment, OrderPaymentModel> {

    private final PaymentMethodTransformer paymentMethodTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;
    private final EmployeeTransformer employeeTransformer;

    @Override
    public OrderPayment toEntity(OrderPaymentModel model) {
        if (model == null)
            return null;

        return OrderPayment.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .paymentNumber(model.getPaymentNumber())
                .orderId(model.getOrderId())
                .paymentMethod(paymentMethodTransformer.toEntity(model.getPaymentMethodModel()))
                .status(model.getStatus())
                .amount(model.getAmount())
                .transactionReference(model.getTransactionReference())
                .paymentNote(model.getPaymentNote())
                .paidAt(model.getPaidAt())
                .refundedAt(model.getRefundedAt())
                .refundReason(model.getRefundReason())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .receivedBy(employeeTransformer.toEntity(model.getReceivedByModel()))
                .refundedBy(employeeTransformer.toEntity(model.getRefundedByModel()))
                .build();
    }

    @Override
    public OrderPaymentModel toModel(OrderPayment entity) {
        if (entity == null)
            return null;

        return OrderPaymentModel.builder()
                .id(entity.getId())
                .paymentNumber(entity.getPaymentNumber())
                .orderId(entity.getOrderId())
                .paymentMethodModel(paymentMethodTransformer.toModel(entity.getPaymentMethod()))
                .status(entity.getStatus())
                .amount(entity.getAmount())
                .transactionReference(entity.getTransactionReference())
                .paymentNote(entity.getPaymentNote())
                .paidAt(entity.getPaidAt())
                .refundedAt(entity.getRefundedAt())
                .refundReason(entity.getRefundReason())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .receivedByModel(employeeTransformer.toModel(entity.getReceivedBy()))
                .refundedByModel(employeeTransformer.toModel(entity.getRefundedBy()))
                .build();
    }
}