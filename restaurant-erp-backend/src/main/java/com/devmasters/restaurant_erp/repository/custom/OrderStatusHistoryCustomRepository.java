package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderStatusHistory;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderStatusHistorySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderStatusHistoryCustomRepository {

    Page<OrderStatusHistory> search(OrderStatusHistorySearchCriteria criteria, Pageable pageable);
}