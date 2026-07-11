package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Menu.Modifier;
import com.devmasters.restaurant_erp.model.searchcriteria.ModifierSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ModifierCustomRepository {

    Page<Modifier> search(
            ModifierSearchCriteria criteria,
            Pageable pageable);
}
