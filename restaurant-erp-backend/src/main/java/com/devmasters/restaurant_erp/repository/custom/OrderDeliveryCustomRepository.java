package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.order.OrderDelivery;
import com.devmasters.restaurant_erp.model.searchcriteria.OrderDeliverySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDeliveryCustomRepository {

    Page<OrderDelivery> search(OrderDeliverySearchCriteria criteria, Pageable pageable);
}