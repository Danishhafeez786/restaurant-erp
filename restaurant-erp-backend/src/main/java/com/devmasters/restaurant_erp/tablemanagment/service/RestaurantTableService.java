package com.devmasters.restaurant_erp.tablemanagment.service;

import com.devmasters.restaurant_erp.tablemanagment.domain.RestaurantTable;
import com.devmasters.restaurant_erp.tablemanagment.model.searchCriteria.RestaurantTableSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RestaurantTableService {

    boolean existsByTableNumberIgnoreCaseAndBranch_Id(String tableNumber, UUID branchId);

    boolean existsByTableNameIgnoreCaseAndBranch_Id(String tableName, UUID branchId);

    RestaurantTable create(RestaurantTable entity);

    Page<RestaurantTable> search(RestaurantTableSearchCriteria criteria, Pageable pageable);

    RestaurantTable findById(UUID id);

    RestaurantTable update(UUID id, RestaurantTable entity);

    RestaurantTable delete(UUID id);

    RestaurantTable restore(UUID id);

    boolean existsByTableNumberIgnoreCaseAndBranch_IdAndIdNot(String tableNumber, UUID branchId, UUID id);

    boolean existsByTableNameIgnoreCaseAndBranch_IdAndIdNot(String tableName, UUID branchId, UUID id);
}