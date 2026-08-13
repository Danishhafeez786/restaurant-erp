package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.order.OrderStatusHistory;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderStatusHistorySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderStatusHistoryService {

    OrderStatusHistory create(OrderStatusHistory entity);

    Page<OrderStatusHistory> search(OrderStatusHistorySearchCriteria criteria, Pageable pageable);

    OrderStatusHistory findById(UUID id);
}