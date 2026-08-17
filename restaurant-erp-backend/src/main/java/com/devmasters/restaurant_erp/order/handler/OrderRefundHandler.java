package com.devmasters.restaurant_erp.order.handler;

import com.devmasters.restaurant_erp.order.domain.OrderRefund;
import com.devmasters.restaurant_erp.order.model.OrderRefundModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderRefundSearchCriteria;
import com.devmasters.restaurant_erp.order.service.OrderRefundService;
import com.devmasters.restaurant_erp.order.transformer.OrderRefundTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRefundHandler {

    private final OrderRefundService orderRefundService;
    private final OrderRefundTransformer orderRefundTransformer;

    public OrderRefundModel create(OrderRefundModel model) {
        if (model == null) {
            throw new RuntimeException("Refund data is required.");
        }

        OrderRefund entity = orderRefundTransformer.toEntity(model);

        entity.setRefundNumber(generateRefundNumber());
        entity.setIsActive(true);

        return orderRefundTransformer.toModel(orderRefundService.create(entity));
    }

    public OrderRefundModel getById(UUID id) {
        return orderRefundTransformer.toModel(orderRefundService.findById(id));
    }

    public PageResponse<OrderRefundModel> search(OrderRefundSearchCriteria criteria, Pageable pageable) {

        Page<OrderRefund> page = orderRefundService.search(criteria, pageable);

        return PageResponse.<OrderRefundModel>builder().content(orderRefundTransformer.toModels(page.getContent())).totalElements(page.getTotalElements()).totalPages(page.getTotalPages()).page(page.getNumber()).size(page.getSize()).first(page.isFirst()).last(page.isLast()).build();
    }

    public OrderRefundModel update(UUID id, OrderRefundModel model) {

        OrderRefund entity = orderRefundTransformer.toEntity(model);

        return orderRefundTransformer.toModel(orderRefundService.update(id, entity));
    }

    public OrderRefundModel delete(UUID id) {
        return orderRefundTransformer.toModel(orderRefundService.delete(id));
    }

    public OrderRefundModel restore(UUID id) {
        return orderRefundTransformer.toModel(orderRefundService.restore(id));
    }

    private String generateRefundNumber() {
        return "REF-" + System.currentTimeMillis();
    }

    public OrderRefundModel processRefund(UUID id, UUID processedById) {

        return orderRefundTransformer.toModel(orderRefundService.processRefund(id, processedById));
    }
}