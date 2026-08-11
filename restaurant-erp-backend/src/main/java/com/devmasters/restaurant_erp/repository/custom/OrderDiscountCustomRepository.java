package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderDiscount;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDiscountSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDiscountCustomRepository {

    Page<OrderDiscount> search(OrderDiscountSearchCriteria criteria, Pageable pageable);
}