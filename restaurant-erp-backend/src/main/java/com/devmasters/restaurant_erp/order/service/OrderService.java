package com.devmasters.restaurant_erp.order.service;

import com.devmasters.restaurant_erp.order.domain.Order;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    Order create(Order entity);

    Page<Order> search(OrderSearchCriteria criteria, Pageable pageable);

    Order findById(UUID id);

    Order update(UUID id, Order entity);

    Order confirm(UUID id);

    Order startPreparing(UUID id);

    Order markReady(UUID id);

    Order complete(UUID id);

    Order cancel(UUID id, String cancellationReason);
}