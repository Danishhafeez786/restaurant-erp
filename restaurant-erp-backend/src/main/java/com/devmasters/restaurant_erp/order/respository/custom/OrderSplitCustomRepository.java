package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderSplit;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderSplitSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderSplitCustomRepository {
    Page<OrderSplit> search(OrderSplitSearchCriteria criteria, Pageable pageable);
}