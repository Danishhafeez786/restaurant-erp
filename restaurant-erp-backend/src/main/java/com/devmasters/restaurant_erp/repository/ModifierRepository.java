package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Menu.Modifier;
import com.devmasters.restaurant_erp.repository.custom.ModifierCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ModifierRepository extends MongoRepository<Modifier, UUID>, ModifierCustomRepository {

    boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId);
    boolean existsByNameIgnoreCaseAndModifierGroup_Id(String name, UUID modifierGroupId);
    boolean existsBySkuIgnoreCaseAndBranch_Id(String sku, UUID branchId);
}
