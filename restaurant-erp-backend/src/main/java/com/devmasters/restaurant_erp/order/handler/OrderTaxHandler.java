package com.devmasters.restaurant_erp.order.handler;

import com.devmasters.restaurant_erp.order.domain.OrderTax;
import com.devmasters.restaurant_erp.order.model.OrderTaxModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderTaxSearchCriteria;
import com.devmasters.restaurant_erp.order.service.OrderTaxService;
import com.devmasters.restaurant_erp.order.transformer.OrderTaxTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderTaxHandler {

    private final OrderTaxService orderTaxService;
    private final OrderTaxTransformer orderTaxTransformer;

    public OrderTaxModel create(OrderTaxModel model) {
        UUID orderId = model.getOrderModel().getId();
        UUID taxId = model.getTaxModel().getId();

        if (orderTaxService.existsByOrderAndTax(orderId, taxId))
            throw new RuntimeException("Tax is already applied to this order.");

        if (model.getTaxNumber() != null && orderTaxService.existsByTaxNumberIgnoreCase(model.getTaxNumber()))
            throw new RuntimeException("Order tax already exists with number : " + model.getTaxNumber());

        OrderTax entity = orderTaxTransformer.toEntity(model);
        OrderTax saved = orderTaxService.create(entity);

        return orderTaxTransformer.toModel(saved);
    }

    public PageResponse<OrderTaxModel> getAll(OrderTaxSearchCriteria criteria, Pageable pageable) {
        Page<OrderTax> page = orderTaxService.search(criteria, pageable);

        return PageResponse.<OrderTaxModel>builder()
                .content(orderTaxTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public OrderTaxModel getById(UUID id) {
        return orderTaxTransformer.toModel(orderTaxService.findById(id));
    }

    public OrderTaxModel update(UUID id, OrderTaxModel model) {
        OrderTax existing = orderTaxService.findById(id);

        if (model.getOrderModel() != null && model.getTaxModel() != null) {
            UUID orderId = model.getOrderModel().getId();
            UUID taxId = model.getTaxModel().getId();

            if (orderTaxService.existsByOrderAndTaxAndIdNot(orderId, taxId, id))
                throw new RuntimeException("Tax is already applied to this order.");
        }

        OrderTax entity = orderTaxTransformer.toEntity(model);
        OrderTax updated = orderTaxService.update(id, entity);

        return orderTaxTransformer.toModel(updated);
    }

    public OrderTaxModel delete(UUID id) {
        OrderTax existing = orderTaxService.findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive()))
            throw new RuntimeException("Order tax already deleted.");

        return orderTaxTransformer.toModel(orderTaxService.delete(id));
    }

    public OrderTaxModel restore(UUID id) {
        OrderTax existing = orderTaxService.findById(id);

        if (Boolean.TRUE.equals(existing.getIsActive()))
            throw new RuntimeException("Order tax is already active.");

        return orderTaxTransformer.toModel(orderTaxService.restore(id));
    }
}