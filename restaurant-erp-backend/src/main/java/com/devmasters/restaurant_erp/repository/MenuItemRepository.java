package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Menu.MenuItem;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.repository.custom.MenuItemCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, UUID>, MenuItemCustomRepository {

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByNameIgnoreCaseAndBranch_Id(String name, UUID branchId);
    boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId);

}