package com.devmasters.restaurant_erp.menu.respository.custom;

import com.devmasters.restaurant_erp.menu.domain.MenuItem;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.MenuItemSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuItemCustomRepository {

    Page<MenuItem> search(
            MenuItemSearchCriteria criteria,
            Pageable pageable
    );
}
