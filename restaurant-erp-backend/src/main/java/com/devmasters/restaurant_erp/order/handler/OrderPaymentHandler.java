package com.devmasters.restaurant_erp.order.handler;

import com.devmasters.restaurant_erp.order.domain.OrderPayment;
import com.devmasters.restaurant_erp.order.model.OrderPaymentModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderPaymentSearchCriteria;
import com.devmasters.restaurant_erp.order.service.OrderPaymentService;
import com.devmasters.restaurant_erp.common.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.order.transformer.OrderPaymentTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderPaymentHandler {

    private final OrderPaymentService orderPaymentService;
    private final OrderPaymentTransformer orderPaymentTransformer;
    private final CodeGeneratorService codeGeneratorService;

    public OrderPaymentModel create(OrderPaymentModel model) {
        validateCreate(model);

        UUID organizationId = model.getOrganizationModel().getId();
        UUID branchId = model.getBranchModel().getId();

        model.setPaymentNumber(
                codeGeneratorService.generatePaymentCode(organizationId)
        );

        OrderPayment entity = orderPaymentTransformer.toEntity(model);
        OrderPayment saved = orderPaymentService.create(entity);

        return orderPaymentTransformer.toModel(saved);
    }

    public PageResponse<OrderPaymentModel> getAll(
            OrderPaymentSearchCriteria criteria,
            Pageable pageable) {

        Page<OrderPayment> page =
                orderPaymentService.search(criteria, pageable);

        return PageResponse.<OrderPaymentModel>builder()
                .content(orderPaymentTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public OrderPaymentModel update(UUID id, OrderPaymentModel model) {
        OrderPayment existing = orderPaymentService.findById(id);

        if (existing.getStatus() != null &&
                existing.getStatus().name().equals("PAID")) {
            throw new RuntimeException(
                    "Paid payment cannot be modified."
            );
        }

        validateUpdate(model, existing);

        OrderPayment entity = orderPaymentTransformer.toEntity(model);

        OrderPayment updated =
                orderPaymentService.update(id, entity);

        return orderPaymentTransformer.toModel(updated);
    }

    public OrderPaymentModel delete(UUID id) {
        OrderPayment payment = orderPaymentService.findById(id);

        if (!Boolean.TRUE.equals(payment.getIsActive()))
            throw new RuntimeException("Order payment already deleted.");

        if (payment.getStatus() != null && payment.getStatus().name().equals("PAID"))
            throw new RuntimeException("Paid payment cannot be deleted.");

        OrderPayment deleted = orderPaymentService.delete(id);
        return orderPaymentTransformer.toModel(deleted);
    }

    public OrderPaymentModel restore(UUID id) {
        OrderPayment payment = orderPaymentService.findById(id);

        if (Boolean.TRUE.equals(payment.getIsActive()))
            throw new RuntimeException("Order payment is already active.");

        OrderPayment restored = orderPaymentService.restore(id);
        return orderPaymentTransformer.toModel(restored);
    }

    public OrderPaymentModel getById(UUID id) {
        OrderPayment payment =
                orderPaymentService.findById(id);

        return orderPaymentTransformer.toModel(payment);
    }

    private void validateCreate(OrderPaymentModel model) {
        if (model == null) {
            throw new RuntimeException("Payment data is required.");
        }

        if (model.getOrderId() == null) {
            throw new RuntimeException("Order is required.");
        }

        if (model.getOrganizationModel() == null ||
                model.getOrganizationModel().getId() == null) {
            throw new RuntimeException("Organization is required.");
        }

        if (model.getBranchModel() == null ||
                model.getBranchModel().getId() == null) {
            throw new RuntimeException("Branch is required.");
        }

        if (model.getPaymentMethodModel() == null ||
                model.getPaymentMethodModel().getId() == null) {
            throw new RuntimeException("Payment method is required.");
        }

        if (model.getAmount() == null ||
                model.getAmount().signum() <= 0) {
            throw new RuntimeException(
                    "Payment amount must be greater than zero."
            );
        }

        if (model.getTransactionReference() != null &&
                !model.getTransactionReference().isBlank() &&
                orderPaymentService.existsByTransactionReferenceIgnoreCase(
                        model.getTransactionReference(),
                        model.getOrganizationModel().getId())) {

            throw new RuntimeException(
                    "Payment already exists with transaction reference: "
                            + model.getTransactionReference()
            );
        }
    }

    private void validateUpdate(
            OrderPaymentModel model,
            OrderPayment existing) {

        if (model.getAmount() == null ||
                model.getAmount().signum() <= 0) {

            throw new RuntimeException(
                    "Payment amount must be greater than zero."
            );
        }

        if (model.getTransactionReference() != null &&
                !model.getTransactionReference().isBlank() &&
                !model.getTransactionReference()
                        .equalsIgnoreCase(existing.getTransactionReference()) &&
                orderPaymentService.existsByTransactionReferenceIgnoreCaseAndIdNot(
                        model.getTransactionReference(),
                        existing.getOrganization().getId(),
                        existing.getId())) {

            throw new RuntimeException(
                    "Payment already exists with transaction reference: "
                            + model.getTransactionReference()
            );
        }
    }
}