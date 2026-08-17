package com.devmasters.restaurant_erp.tablemanagment.respository.custom;

import com.devmasters.restaurant_erp.tablemanagment.domain.RestaurantTable;
import com.devmasters.restaurant_erp.tablemanagment.model.searchCriteria.RestaurantTableSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantTableCustomRepository {

    Page<RestaurantTable> search(RestaurantTableSearchCriteria criteria, Pageable pageable);
}
