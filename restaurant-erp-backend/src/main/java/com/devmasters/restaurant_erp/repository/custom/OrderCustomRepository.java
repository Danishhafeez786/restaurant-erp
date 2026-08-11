package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.Order;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCustomRepository {

    Page<Order> search(OrderSearchCriteria criteria, Pageable pageable);
}