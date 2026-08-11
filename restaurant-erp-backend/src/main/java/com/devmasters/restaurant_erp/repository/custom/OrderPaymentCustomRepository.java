package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderPayment;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderPaymentSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderPaymentCustomRepository {

    Page<OrderPayment> search(OrderPaymentSearchCriteria criteria, Pageable pageable);
}