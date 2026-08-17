package com.devmasters.restaurant_erp.order.service;

import com.devmasters.restaurant_erp.order.domain.OrderDiscount;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderDiscountSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderDiscountService {

    boolean existsByDiscountNumberIgnoreCase(String discountNumber);

    boolean existsByOrderAndDiscountName(UUID orderId, String discountName);

    boolean existsByOrderAndDiscountNameAndIdNot(UUID orderId, String discountName, UUID id);

    OrderDiscount create(OrderDiscount entity);

    Page<OrderDiscount> search(OrderDiscountSearchCriteria criteria, Pageable pageable);

    OrderDiscount findById(UUID id);

    OrderDiscount update(UUID id, OrderDiscount entity);

    OrderDiscount delete(UUID id);

    OrderDiscount restore(UUID id);
}