package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Menu.ModifierGroup;
import com.devmasters.restaurant_erp.model.searchcriteria.ModifierGroupSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ModifierGroupCustomRepository {

    Page<ModifierGroup> search(ModifierGroupSearchCriteria criteria, Pageable pageable);
}
