package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderKitchenTicket;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderKitchenTicketSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderKitchenTicketCustomRepository {

    Page<OrderKitchenTicket> search(OrderKitchenTicketSearchCriteria criteria, Pageable pageable);
}