package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Menu.MenuItemModifierGroup;
import com.devmasters.restaurant_erp.model.searchcriteria.MenuItemModifierGroupSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuItemModifierGroupCustomRepository {

    Page<MenuItemModifierGroup> search(MenuItemModifierGroupSearchCriteria criteria, Pageable pageable);
}
