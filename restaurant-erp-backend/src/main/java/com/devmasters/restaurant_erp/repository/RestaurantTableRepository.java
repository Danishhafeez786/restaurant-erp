package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.RestaurantTable;
import com.devmasters.restaurant_erp.repository.custom.RestaurantTableCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantTableRepository extends MongoRepository<RestaurantTable, UUID>, RestaurantTableCustomRepository {

    boolean existsByTableNumberIgnoreCaseAndBranch_Id(String tableNumber, UUID branchId);

    boolean existsByTableNameIgnoreCaseAndBranch_Id(String tableName, UUID branchId);

    boolean existsByTableNumberIgnoreCaseAndBranch_IdAndIdNot(String tableNumber, UUID branchId, UUID id);

    boolean existsByTableNameIgnoreCaseAndBranch_IdAndIdNot(String tableName, UUID branchId, UUID id);
}