package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderKitchenTicket;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderKitchenTicketSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderKitchenTicketCustomRepository {

    Page<OrderKitchenTicket> search(OrderKitchenTicketSearchCriteria criteria, Pageable pageable);
}