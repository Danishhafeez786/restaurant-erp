package com.devmasters.restaurant_erp.repository;

import com.devmasters.restaurant_erp.domain.Menu.MenuItemModifierGroup;
import com.devmasters.restaurant_erp.repository.custom.MenuItemModifierGroupCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuItemModifierGroupRepository extends MongoRepository<MenuItemModifierGroup, UUID>, MenuItemModifierGroupCustomRepository {

    boolean existsByMenuItem_IdAndModifierGroup_Id(UUID menuItemId, UUID modifierGroupId);
}
