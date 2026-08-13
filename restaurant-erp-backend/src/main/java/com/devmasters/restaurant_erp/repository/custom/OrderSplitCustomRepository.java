package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderSplit;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderSplitSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderSplitCustomRepository {
    Page<OrderSplit> search(OrderSplitSearchCriteria criteria, Pageable pageable);
}