package com.devmasters.restaurant_erp.menu.transformer;

import com.devmasters.restaurant_erp.branch.transformer.BranchTransformer;
import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.menu.domain.Category;
import com.devmasters.restaurant_erp.menu.model.CategoryModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CategoryTransformer extends Transformer<Category, CategoryModel> {

    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    @Override
    public Category toEntity(CategoryModel model) {

        if (model == null)
            return null;

        return Category.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .categoryCode(model.getCategoryCode())
                .categoryName(model.getCategoryName())
                .description(model.getDescription())
                .imageUrl(model.getImageUrl())
                .displayOrder(model.getDisplayOrder())
                .available(model.getAvailable())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .isActive(model.getIsActive())
                .build();
    }

    @Override
    public CategoryModel toModel(Category entity) {

        if (entity == null)
            return null;

        return CategoryModel.builder()
                .id(entity.getId())
                .categoryCode(entity.getCategoryCode())
                .categoryName(entity.getCategoryName())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .displayOrder(entity.getDisplayOrder())
                .available(entity.getAvailable())
                .organizationModel(
                        organizationTransformer.toModel(
                                entity.getOrganization()))
                .branchModel(
                        branchTransformer.toModel(
                                entity.getBranch()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}