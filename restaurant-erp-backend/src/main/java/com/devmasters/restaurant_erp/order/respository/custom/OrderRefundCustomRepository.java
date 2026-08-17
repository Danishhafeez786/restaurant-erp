package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderRefund;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderRefundSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRefundCustomRepository {
    Page<OrderRefund> search(OrderRefundSearchCriteria criteria, Pageable pageable);
}