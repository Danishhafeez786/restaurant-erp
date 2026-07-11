package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Menu.MenuItem;
import com.devmasters.restaurant_erp.model.searchcriteria.MenuItemSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuItemCustomRepository {

    Page<MenuItem> search(
            MenuItemSearchCriteria criteria,
            Pageable pageable
    );
}
