package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.order.OrderDiscount;
import com.devmasters.restaurant_erp.model.order.OrderDiscountModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDiscountSearchCriteria;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.service.OrderDiscountService;
import com.devmasters.restaurant_erp.transformer.OrderDiscountTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderDiscountHandler {

    private final OrderDiscountService orderDiscountService;
    private final OrderDiscountTransformer orderDiscountTransformer;
    private final CodeGeneratorService codeGeneratorService;

    public OrderDiscountModel create(OrderDiscountModel model) {
        if (model.getOrderModel() == null || model.getOrderModel().getId() == null)
            throw new RuntimeException("Order is required.");

        if (model.getOrganizationModel() == null || model.getOrganizationModel().getId() == null)
            throw new RuntimeException("Organization is required.");

        if (model.getDiscountName() == null || model.getDiscountName().isBlank())
            throw new RuntimeException("Discount name is required.");

        if (model.getDiscountType() == null)
            throw new RuntimeException("Discount type is required.");

        if (model.getDiscountValue() == null || model.getDiscountValue().signum() < 0)
            throw new RuntimeException("Discount value must be greater than or equal to zero.");

        if (model.getDiscountAmount() == null || model.getDiscountAmount().signum() < 0)
            throw new RuntimeException("Discount amount must be greater than or equal to zero.");

        UUID orderId = model.getOrderModel().getId();

        if (orderDiscountService.existsByOrderAndDiscountName(orderId, model.getDiscountName()))
            throw new RuntimeException("Discount already exists on this order.");

        model.setDiscountNumber(
                codeGeneratorService.generateOrderDiscountCode(
                        model.getOrganizationModel().getId()
                )
        );

        OrderDiscount entity = orderDiscountTransformer.toEntity(model);
        OrderDiscount saved = orderDiscountService.create(entity);

        return orderDiscountTransformer.toModel(saved);
    }

    public PageResponse<OrderDiscountModel> getAll(OrderDiscountSearchCriteria criteria, Pageable pageable) {
        Page<OrderDiscount> page = orderDiscountService.search(criteria, pageable);

        return PageResponse.<OrderDiscountModel>builder()
                .content(orderDiscountTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public OrderDiscountModel getById(UUID id) {
        return orderDiscountTransformer.toModel(orderDiscountService.findById(id));
    }

    public OrderDiscountModel update(UUID id, OrderDiscountModel model) {
        OrderDiscount existing = orderDiscountService.findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive()))
            throw new RuntimeException("Cannot update an inactive order discount.");

        if (model.getDiscountName() == null || model.getDiscountName().isBlank())
            throw new RuntimeException("Discount name is required.");

        if (model.getDiscountType() == null)
            throw new RuntimeException("Discount type is required.");

        if (model.getDiscountValue() == null || model.getDiscountValue().signum() < 0)
            throw new RuntimeException("Discount value must be greater than or equal to zero.");

        if (model.getDiscountAmount() == null || model.getDiscountAmount().signum() < 0)
            throw new RuntimeException("Discount amount must be greater than or equal to zero.");

        UUID orderId = existing.getOrder().getId();

        if (orderDiscountService.existsByOrderAndDiscountNameAndIdNot(
                orderId,
                model.getDiscountName(),
                id))
            throw new RuntimeException("Discount already exists on this order.");

        OrderDiscount entity = orderDiscountTransformer.toEntity(model);
        OrderDiscount updated = orderDiscountService.update(id, entity);

        return orderDiscountTransformer.toModel(updated);
    }

    public OrderDiscountModel delete(UUID id) {
        OrderDiscount existing = orderDiscountService.findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive()))
            throw new RuntimeException("Order discount already deleted.");

        return orderDiscountTransformer.toModel(orderDiscountService.delete(id));
    }

    public OrderDiscountModel restore(UUID id) {
        OrderDiscount existing = orderDiscountService.findById(id);

        if (Boolean.TRUE.equals(existing.getIsActive()))
            throw new RuntimeException("Order discount is already active.");

        return orderDiscountTransformer.toModel(orderDiscountService.restore(id));
    }
}