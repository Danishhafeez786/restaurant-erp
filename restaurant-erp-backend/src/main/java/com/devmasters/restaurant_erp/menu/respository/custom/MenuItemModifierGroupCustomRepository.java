package com.devmasters.restaurant_erp.menu.respository.custom;

import com.devmasters.restaurant_erp.menu.domain.MenuItemModifierGroup;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.MenuItemModifierGroupSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuItemModifierGroupCustomRepository {

    Page<MenuItemModifierGroup> search(MenuItemModifierGroupSearchCriteria criteria, Pageable pageable);
}
