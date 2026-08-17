package com.devmasters.restaurant_erp.order.service;

import com.devmasters.restaurant_erp.order.domain.OrderKitchenTicket;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderKitchenTicketSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderKitchenTicketService {

    boolean existsByOrderId(UUID orderId);

    OrderKitchenTicket create(OrderKitchenTicket entity);

    Page<OrderKitchenTicket> search(OrderKitchenTicketSearchCriteria criteria, Pageable pageable);

    OrderKitchenTicket findById(UUID id);

    OrderKitchenTicket update(UUID id, OrderKitchenTicket entity);

    OrderKitchenTicket delete(UUID id);

    OrderKitchenTicket restore(UUID id);
}