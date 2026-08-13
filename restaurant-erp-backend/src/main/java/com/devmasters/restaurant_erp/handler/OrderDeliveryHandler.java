package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.order.OrderDelivery;
import com.devmasters.restaurant_erp.model.order.OrderDeliveryModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDeliverySearchCriteria;
import com.devmasters.restaurant_erp.service.OrderDeliveryService;
import com.devmasters.restaurant_erp.transformer.OrderDeliveryTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderDeliveryHandler {

    private final OrderDeliveryService orderDeliveryService;
    private final OrderDeliveryTransformer orderDeliveryTransformer;

    public OrderDeliveryModel create(OrderDeliveryModel model) {
        if (model.getOrderModel() == null || model.getOrderModel().getId() == null)
            throw new RuntimeException("Order is required.");

        if (model.getOrganizationModel() == null || model.getOrganizationModel().getId() == null)
            throw new RuntimeException("Organization is required.");

        if (model.getBranchModel() == null || model.getBranchModel().getId() == null)
            throw new RuntimeException("Branch is required.");

        if (model.getDeliveryAddress() == null)
            throw new RuntimeException("Delivery address is required.");

        UUID orderId = model.getOrderModel().getId();

        if (orderDeliveryService.existsByOrderId(orderId))
            throw new RuntimeException("Delivery already exists for this order.");

        OrderDelivery entity = orderDeliveryTransformer.toEntity(model);
        OrderDelivery saved = orderDeliveryService.create(entity);

        return orderDeliveryTransformer.toModel(saved);
    }

    public PageResponse<OrderDeliveryModel> getAll(
            OrderDeliverySearchCriteria criteria,
            Pageable pageable) {

        Page<OrderDelivery> page = orderDeliveryService.search(criteria, pageable);

        return PageResponse.<OrderDeliveryModel>builder()
                .content(orderDeliveryTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public OrderDeliveryModel getById(UUID id) {
        return orderDeliveryTransformer.toModel(
                orderDeliveryService.findById(id)
        );
    }

    public OrderDeliveryModel update(UUID id, OrderDeliveryModel model) {
        OrderDelivery existing = orderDeliveryService.findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive()))
            throw new RuntimeException("Cannot update an inactive delivery.");

        if (existing.getStatus() == com.devmasters.restaurant_erp.enums.DeliveryStatus.DELIVERED)
            throw new RuntimeException("Delivered order cannot be updated.");

        if (model.getDeliveryAddress() == null)
            throw new RuntimeException("Delivery address is required.");

        OrderDelivery entity = orderDeliveryTransformer.toEntity(model);
        OrderDelivery updated = orderDeliveryService.update(id, entity);

        return orderDeliveryTransformer.toModel(updated);
    }

    public OrderDeliveryModel delete(UUID id) {
        OrderDelivery existing = orderDeliveryService.findById(id);

        if (!Boolean.TRUE.equals(existing.getIsActive()))
            throw new RuntimeException("Order delivery already deleted.");

        if (existing.getStatus() == com.devmasters.restaurant_erp.enums.DeliveryStatus.DELIVERED)
            throw new RuntimeException("Delivered order cannot be deleted.");

        return orderDeliveryTransformer.toModel(
                orderDeliveryService.delete(id)
        );
    }

    public OrderDeliveryModel restore(UUID id) {
        OrderDelivery existing = orderDeliveryService.findById(id);

        if (Boolean.TRUE.equals(existing.getIsActive()))
            throw new RuntimeException("Order delivery is already active.");

        return orderDeliveryTransformer.toModel(
                orderDeliveryService.restore(id)
        );
    }
}