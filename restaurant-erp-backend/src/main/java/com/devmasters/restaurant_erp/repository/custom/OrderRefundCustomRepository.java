package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderRefund;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderRefundSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRefundCustomRepository {
    Page<OrderRefund> search(OrderRefundSearchCriteria criteria, Pageable pageable);
}