package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.order.OrderDelivery;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDeliverySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderDeliveryService {

    boolean existsByOrderId(UUID orderId);

    OrderDelivery create(OrderDelivery entity);

    Page<OrderDelivery> search(OrderDeliverySearchCriteria criteria, Pageable pageable);

    OrderDelivery findById(UUID id);

    OrderDelivery update(UUID id, OrderDelivery entity);

    OrderDelivery delete(UUID id);

    OrderDelivery restore(UUID id);
}