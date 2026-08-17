package com.devmasters.restaurant_erp.menu.service;

import com.devmasters.restaurant_erp.menu.domain.Category;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.CategorySearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryService {

    boolean existsByCategoryCodeIgnoreCaseAndBranch_Id(String categoryCode, UUID branchId);

    boolean existsByCategoryNameIgnoreCaseAndBranch_Id(String categoryName, UUID branchId);

    Category create(Category entity);

    Page<Category> search(CategorySearchCriteria criteria, Pageable pageable);

    Category findById(UUID id);

    Category update(UUID id, Category entity);

    Category delete(UUID id);

    Category restore(UUID id);
}
