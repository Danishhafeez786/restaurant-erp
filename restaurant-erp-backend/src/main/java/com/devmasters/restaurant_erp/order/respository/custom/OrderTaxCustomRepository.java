package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderTax;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderTaxSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderTaxCustomRepository {

    Page<OrderTax> search(OrderTaxSearchCriteria criteria, Pageable pageable);
}