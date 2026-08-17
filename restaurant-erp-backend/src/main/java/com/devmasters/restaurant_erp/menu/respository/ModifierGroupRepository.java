package com.devmasters.restaurant_erp.menu.respository;

import com.devmasters.restaurant_erp.menu.domain.ModifierGroup;
import com.devmasters.restaurant_erp.menu.respository.custom.ModifierGroupCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ModifierGroupRepository extends MongoRepository<ModifierGroup, UUID>, ModifierGroupCustomRepository {

    boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId);

    boolean existsByNameIgnoreCaseAndBranch_Id(String name, UUID branchId);
}
