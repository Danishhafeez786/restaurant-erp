package com.devmasters.restaurant_erp.repository.custom;

import com.devmasters.restaurant_erp.domain.Menu.Category;
import com.devmasters.restaurant_erp.model.searchcriteria.CategorySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryCustomRepository {

    Page<Category> search(
            CategorySearchCriteria criteria,
            Pageable pageable
    );
}
