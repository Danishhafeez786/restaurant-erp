package com.devmasters.restaurant_erp.menu.respository.custom;

import com.devmasters.restaurant_erp.menu.domain.Category;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.CategorySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryCustomRepository {

    Page<Category> search(CategorySearchCriteria criteria, Pageable pageable);
}
