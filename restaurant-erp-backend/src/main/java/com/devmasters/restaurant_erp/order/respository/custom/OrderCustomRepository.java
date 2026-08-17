package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.Order;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCustomRepository {

    Page<Order> search(OrderSearchCriteria criteria, Pageable pageable);
}