package com.devmasters.restaurant_erp.menu.service;

import com.devmasters.restaurant_erp.menu.domain.MenuItem;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.MenuItemSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MenuItemService {

    boolean existsByCodeIgnoreCaseAndBranch_Id(String code, UUID branchId);

    boolean existsByNameIgnoreCaseAndBranch_Id(String name, UUID branchId);

    MenuItem create(MenuItem entity);

    Page<MenuItem> search(MenuItemSearchCriteria criteria, Pageable pageable);

    MenuItem findById(UUID id);

    MenuItem update(UUID id, MenuItem entity);

    MenuItem delete(UUID id);

    MenuItem restore(UUID id);
}