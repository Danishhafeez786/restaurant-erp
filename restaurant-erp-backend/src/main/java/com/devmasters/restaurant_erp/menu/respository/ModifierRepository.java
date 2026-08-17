package com.devmasters.restaurant_erp.menu.respository;

import com.devmasters.restaurant_erp.menu.domain.Modifier;
import com.devmasters.restaurant_erp.menu.respository.custom.ModifierCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ModifierRepository extends MongoRepository<Modifier, UUID>, ModifierCustomRepository {

    boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId);
    boolean existsByNameIgnoreCaseAndModifierGroup_Id(String name, UUID modifierGroupId);
    boolean existsBySkuIgnoreCaseAndBranch_Id(String sku, UUID branchId);
}
