package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.order.OrderSplit;
import com.devmasters.restaurant_erp.model.order.OrderSplitModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderSplitSearchCriteria;
import com.devmasters.restaurant_erp.service.OrderSplitService;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.transformer.OrderSplitTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderSplitHandler {

    private final OrderSplitService orderSplitService;
    private final OrderSplitTransformer orderSplitTransformer;
    private final CodeGeneratorService codeGeneratorService;

    public OrderSplitModel create(OrderSplitModel model) {
        if (model == null) {
            throw new RuntimeException("Split data is required.");
        }

        String splitNumber = codeGeneratorService.generateSplitNumber();

        model.setSplitNumber(splitNumber);

        OrderSplit entity = orderSplitTransformer.toEntity(model);

        entity.setIsActive(true);

        return orderSplitTransformer.toModel(orderSplitService.create(entity));
    }

    public OrderSplitModel getById(UUID id) {
        return orderSplitTransformer.toModel(orderSplitService.findById(id));
    }

    public PageResponse<OrderSplitModel> search(OrderSplitSearchCriteria criteria, Pageable pageable) {

        Page<OrderSplit> page = orderSplitService.search(criteria, pageable);

        return PageResponse.<OrderSplitModel>builder().content(orderSplitTransformer.toModels(page.getContent())).totalElements(page.getTotalElements()).totalPages(page.getTotalPages()).page(page.getNumber()).size(page.getSize()).first(page.isFirst()).last(page.isLast()).build();
    }

    public OrderSplitModel update(UUID id, OrderSplitModel model) {

        if (model == null) {
            throw new RuntimeException("Split data is required.");
        }

        OrderSplit entity = orderSplitTransformer.toEntity(model);

        return orderSplitTransformer.toModel(orderSplitService.update(id, entity));
    }

    public OrderSplitModel delete(UUID id) {
        return orderSplitTransformer.toModel(orderSplitService.delete(id));
    }

    public OrderSplitModel restore(UUID id) {
        return orderSplitTransformer.toModel(orderSplitService.restore(id));
    }
}