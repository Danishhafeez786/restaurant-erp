package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.order.OrderStatusHistory;
import com.devmasters.restaurant_erp.model.order.OrderStatusHistoryModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderStatusHistorySearchCriteria;
import com.devmasters.restaurant_erp.service.OrderStatusHistoryService;
import com.devmasters.restaurant_erp.transformer.OrderStatusHistoryTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderStatusHistoryHandler {

    private final OrderStatusHistoryService orderStatusHistoryService;
    private final OrderStatusHistoryTransformer orderStatusHistoryTransformer;

    public OrderStatusHistoryModel create(OrderStatusHistoryModel model) {
        if (model.getOrderModel() == null || model.getOrderModel().getId() == null)
            throw new RuntimeException("Order is required.");

        if (model.getNewStatus() == null)
            throw new RuntimeException("New status is required.");

        if (model.getPreviousStatus() == null)
            throw new RuntimeException("Previous status is required.");

        if (model.getPreviousStatus() == model.getNewStatus())
            throw new RuntimeException("Previous and new status cannot be the same.");

        if (model.getChangedAt() == null)
            model.setChangedAt(LocalDateTime.now());

        OrderStatusHistory entity = orderStatusHistoryTransformer.toEntity(model);
        OrderStatusHistory saved = orderStatusHistoryService.create(entity);

        return orderStatusHistoryTransformer.toModel(saved);
    }

    public PageResponse<OrderStatusHistoryModel> getAll(
            OrderStatusHistorySearchCriteria criteria,
            Pageable pageable) {

        Page<OrderStatusHistory> page = orderStatusHistoryService.search(criteria, pageable);

        return PageResponse.<OrderStatusHistoryModel>builder()
                .content(orderStatusHistoryTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public OrderStatusHistoryModel getById(UUID id) {
        return orderStatusHistoryTransformer.toModel(
                orderStatusHistoryService.findById(id)
        );
    }
}