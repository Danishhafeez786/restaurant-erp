package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.order.OrderDiscount;
import com.devmasters.restaurant_erp.model.order.OrderDiscountModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDiscountSearchCriteria;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.service.order.OrderDiscountService;
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
        UUID organizationId = model.getOrganizationModel().getId();

        if (orderDiscountService.existsByDiscountNameIgnoreCase(
                model.getDiscountName(), organizationId))
            throw new RuntimeException("Discount already exists with name : " + model.getDiscountName());

        model.setDiscountNumber(codeGeneratorService.generateOrderDiscountCode(organizationId));

        OrderDiscount entity = orderDiscountTransformer.toEntity(model);
        OrderDiscount saved = orderDiscountService.create(entity);

        return orderDiscountTransformer.toModel(saved);
    }

    public PageResponse<OrderDiscountModel> getAll(
            OrderDiscountSearchCriteria criteria,
            Pageable pageable) {

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
        return orderDiscountTransformer.toModel(
                orderDiscountService.findById(id)
        );
    }

    public OrderDiscountModel update(UUID id, OrderDiscountModel model) {
        OrderDiscount existing = orderDiscountService.findById(id);

        UUID organizationId = existing.getOrganization().getId();

        if (orderDiscountService.existsByDiscountNameIgnoreCaseAndIdNot(
                model.getDiscountName(), organizationId, id))
            throw new RuntimeException("Discount already exists with name : " + model.getDiscountName());

        OrderDiscount entity = orderDiscountTransformer.toEntity(model);
        OrderDiscount updated = orderDiscountService.update(id, entity);

        return orderDiscountTransformer.toModel(updated);
    }

    public OrderDiscountModel delete(UUID id) {
        OrderDiscount discount = orderDiscountService.findById(id);

        if (!Boolean.TRUE.equals(discount.getIsActive()))
            throw new RuntimeException("Order discount already deleted.");

        OrderDiscount deleted = orderDiscountService.delete(id);
        return orderDiscountTransformer.toModel(deleted);
    }

    public OrderDiscountModel restore(UUID id) {
        OrderDiscount discount = orderDiscountService.findById(id);

        if (Boolean.TRUE.equals(discount.getIsActive()))
            throw new RuntimeException("Order discount is already active.");

        OrderDiscount restored = orderDiscountService.restore(id);
        return orderDiscountTransformer.toModel(restored);
    }
}