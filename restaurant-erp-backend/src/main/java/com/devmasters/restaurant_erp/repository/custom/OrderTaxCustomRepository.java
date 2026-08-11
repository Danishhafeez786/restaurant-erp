package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderTax;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderTaxSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderTaxCustomRepository {

    Page<OrderTax> search(OrderTaxSearchCriteria criteria, Pageable pageable);
}