package com.devmasters.restaurant_erp.menu.respository.custom;

import com.devmasters.restaurant_erp.menu.domain.ModifierGroup;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.ModifierGroupSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ModifierGroupCustomRepository {

    Page<ModifierGroup> search(ModifierGroupSearchCriteria criteria, Pageable pageable);
}
