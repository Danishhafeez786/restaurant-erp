package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderDiscount;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderDiscountSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDiscountCustomRepository {

    Page<OrderDiscount> search(OrderDiscountSearchCriteria criteria, Pageable pageable);
}