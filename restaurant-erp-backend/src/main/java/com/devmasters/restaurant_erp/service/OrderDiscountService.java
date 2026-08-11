package com.devmasters.restaurant_erp.service.order;

import com.devmasters.restaurant_erp.domain.order.OrderDiscount;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDiscountSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderDiscountService {

    boolean existsByDiscountNumberIgnoreCase(String discountNumber, UUID organizationId);

    boolean existsByDiscountNameIgnoreCase(String discountName, UUID organizationId);

    boolean existsByDiscountNumberIgnoreCaseAndIdNot(String discountNumber, UUID organizationId, UUID id);

    boolean existsByDiscountNameIgnoreCaseAndIdNot(String discountName, UUID organizationId, UUID id);

    boolean existsByOrderId(UUID orderId, UUID organizationId);

    OrderDiscount create(OrderDiscount entity);

    Page<OrderDiscount> search(OrderDiscountSearchCriteria criteria, Pageable pageable);

    OrderDiscount findById(UUID id);

    OrderDiscount update(UUID id, OrderDiscount entity);

    OrderDiscount delete(UUID id);

    OrderDiscount restore(UUID id);
}