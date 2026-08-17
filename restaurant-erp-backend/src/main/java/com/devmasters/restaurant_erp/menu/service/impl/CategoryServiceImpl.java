package com.devmasters.restaurant_erp.menu.service.impl;

import com.devmasters.restaurant_erp.menu.domain.Category;
import com.devmasters.restaurant_erp.menu.model.searchCriteria.CategorySearchCriteria;
import com.devmasters.restaurant_erp.menu.respository.CategoryRepository;
import com.devmasters.restaurant_erp.menu.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public boolean existsByCategoryCodeIgnoreCaseAndBranch_Id(String categoryCode, UUID branchId) {
        return categoryRepository
                .existsByCategoryCodeIgnoreCaseAndBranch_Id(
                        categoryCode,
                        branchId);
    }

    @Override
    public boolean existsByCategoryNameIgnoreCaseAndBranch_Id(String categoryName, UUID branchId) {
        return categoryRepository
                .existsByCategoryNameIgnoreCaseAndBranch_Id(
                        categoryName,
                        branchId);
    }

    @Override
    public Category create(Category entity) {
        entity.setCreatedAt(LocalDateTime.now());
        return categoryRepository.save(entity);
    }

    @Override
    public Page<Category> search(CategorySearchCriteria criteria, Pageable pageable) {
        return categoryRepository.search(criteria, pageable);
    }

    @Override
    public Category findById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                        new RuntimeException("Category not found."));
    }

    @Override
    public Category update(UUID id, Category entity) {

        Category existing = findById(id);
        existing.setCategoryCode(entity.getCategoryCode());
        existing.setCategoryName(entity.getCategoryName());
        existing.setDescription(entity.getDescription());
        existing.setImageUrl(entity.getImageUrl());
        existing.setDisplayOrder(entity.getDisplayOrder());
        existing.setAvailable(entity.getAvailable());
        existing.setOrganization(entity.getOrganization());
        existing.setBranch(entity.getBranch());
        existing.setIsActive(entity.getIsActive());

        return categoryRepository.save(existing);
    }

    @Override
    public Category delete(UUID id) {

        Category category = findById(id);
        if (!Boolean.TRUE.equals(category.getIsActive())) {
            throw new RuntimeException("Category already deleted.");
        }
        category.setIsActive(false);
        return categoryRepository.save(category);
    }

    @Override
    public Category restore(UUID id) {

        Category category = findById(id);
        category.setIsActive(true);
        return categoryRepository.save(category);
    }
}