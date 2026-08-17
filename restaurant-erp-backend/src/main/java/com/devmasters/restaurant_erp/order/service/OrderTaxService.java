package com.devmasters.restaurant_erp.order.service;

import com.devmasters.restaurant_erp.order.domain.OrderTax;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderTaxSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderTaxService {

    boolean existsByTaxNumberIgnoreCase(String taxNumber);

    boolean existsByOrderAndTax(UUID orderId, UUID taxId);

    boolean existsByOrderAndTaxAndIdNot(UUID orderId, UUID taxId, UUID id);

    OrderTax create(OrderTax entity);

    Page<OrderTax> search(OrderTaxSearchCriteria criteria, Pageable pageable);

    OrderTax findById(UUID id);

    OrderTax update(UUID id, OrderTax entity);

    OrderTax delete(UUID id);

    OrderTax restore(UUID id);
}