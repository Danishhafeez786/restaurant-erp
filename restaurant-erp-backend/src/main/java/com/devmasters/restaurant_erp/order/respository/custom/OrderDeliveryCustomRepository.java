package com.devmasters.restaurant_erp.order.respository.custom;

import com.devmasters.restaurant_erp.order.domain.OrderDelivery;
import com.devmasters.restaurant_erp.order.model.searchCriteria.OrderDeliverySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDeliveryCustomRepository {

    Page<OrderDelivery> search(OrderDeliverySearchCriteria criteria, Pageable pageable);
}