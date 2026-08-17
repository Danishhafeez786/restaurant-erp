package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderPayment;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderPaymentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderPaymentCustomRepository {

    Page<OrderPayment> search(OrderPaymentSearchCriteria criteria, Pageable pageable);
}