package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderStatusHistory;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderStatusHistorySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderStatusHistoryCustomRepository {

    Page<OrderStatusHistory> search(OrderStatusHistorySearchCriteria criteria, Pageable pageable);
}