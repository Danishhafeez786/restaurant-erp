package com.devmasters.restaurant_erp.menu.respository.custom;

import com.devmasters.restaurant_erp.menu.domain.Modifier;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.ModifierSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ModifierCustomRepository {

    Page<Modifier> search(
            ModifierSearchCriteria criteria,
            Pageable pageable);
}
