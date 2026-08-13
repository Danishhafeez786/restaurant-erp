package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.order.OrderKitchenTicket;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderKitchenTicketSearchCriteria;
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