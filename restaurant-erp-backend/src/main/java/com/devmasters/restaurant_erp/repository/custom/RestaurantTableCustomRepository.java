package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.RestaurantTable;
import com.devmasters.restaurant_erp.model.searchcriteria.RestaurantTableSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantTableCustomRepository {

    Page<RestaurantTable> search(RestaurantTableSearchCriteria criteria, Pageable pageable);
}
