package com.devmasters.restaurant_erp.menu.service;

import com.devmasters.restaurant_erp.menu.domain.MenuItemModifierGroup;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.MenuItemModifierGroupSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MenuItemModifierGroupService {

    boolean existsByMenuItem_IdAndModifierGroup_Id(UUID menuItemId, UUID modifierGroupId);

    MenuItemModifierGroup create(MenuItemModifierGroup entity);

    Page<MenuItemModifierGroup> search(MenuItemModifierGroupSearchCriteria criteria, Pageable pageable);

    MenuItemModifierGroup findById(UUID id);

    MenuItemModifierGroup update(UUID id, MenuItemModifierGroup entity);

    MenuItemModifierGroup delete(UUID id);

    MenuItemModifierGroup restore(UUID id);
}
