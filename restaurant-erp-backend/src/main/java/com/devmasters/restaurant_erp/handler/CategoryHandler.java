package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Menu.Category;
import com.devmasters.restaurant_erp.model.Menu.CategoryModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.CategorySearchCriteria;
import com.devmasters.restaurant_erp.service.CategoryService;

import com.devmasters.restaurant_erp.transformer.CategoryTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CategoryHandler {

    private final CategoryService categoryService;
    private final CategoryTransformer categoryTransformer;

    public CategoryModel create(CategoryModel model) {

        if (categoryService.existsByCategoryCodeIgnoreCaseAndBranch_Id(
                model.getCategoryCode(),
                model.getBranchModel().getId())) {
            throw new RuntimeException("Category Code already exists : " + model.getCategoryCode());
        }

        if (categoryService.existsByCategoryNameIgnoreCaseAndBranch_Id(model.getCategoryName(),
                model.getBranchModel().getId())) {
            throw new RuntimeException("Category Name already exists : " + model.getCategoryName());
        }

        Category entity = categoryTransformer.toEntity(model);
        Category saved = categoryService.create(entity);
        return categoryTransformer.toModel(saved);
    }

    public PageResponse<CategoryModel> getAll(
            CategorySearchCriteria criteria,
            Pageable pageable) {

        Page<Category> page = categoryService.search(criteria, pageable);
        return PageResponse.<CategoryModel>builder()
                .content(categoryTransformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public CategoryModel update(UUID id, CategoryModel model) {

        Category entity = categoryTransformer.toEntity(model);
        Category updated = categoryService.update(id, entity);
        return categoryTransformer.toModel(updated);
    }

    public CategoryModel delete(UUID id) {

        Category deleted = categoryService.delete(id);
        return categoryTransformer.toModel(deleted);
    }

    public CategoryModel restore(UUID id) {

        Category restored = categoryService.restore(id);
        return categoryTransformer.toModel(restored);
    }
}
