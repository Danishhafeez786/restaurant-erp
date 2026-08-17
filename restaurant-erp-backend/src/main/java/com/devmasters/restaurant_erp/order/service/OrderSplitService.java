package com.devmasters.restaurant_erp.order.service;

import com.devmasters.restaurant_erp.order.domain.OrderSplit;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderSplitSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderSplitService {

    OrderSplit create(OrderSplit split);
    OrderSplit findById(UUID id);
    Page<OrderSplit> search(OrderSplitSearchCriteria criteria, Pageable pageable);
    List<OrderSplit> findByOrder(UUID orderId, UUID organizationId);
    OrderSplit update(UUID id, OrderSplit split);
    OrderSplit delete(UUID id);
    OrderSplit restore(UUID id);
}