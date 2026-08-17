package com.devmasters.restaurant_erp.expense.transformer;


import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.expense.domain.ExpenseCategory;
import com.devmasters.restaurant_erp.expense.model.ExpenseCategoryModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ExpenseCategoryTransformer extends Transformer<ExpenseCategory, ExpenseCategoryModel> {

    private final OrganizationTransformer organizationTransformer;

    @Override
    public ExpenseCategory toEntity(ExpenseCategoryModel model) {

        if (model == null)
            return null;

        return ExpenseCategory.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .categoryName(model.getCategoryName())
                .categoryCode(model.getCategoryCode())
                .description(model.getDescription())
                .color(model.getColor())
                .icon(model.getIcon())
                .sortOrder(model.getSortOrder())
                .systemDefined(model.getSystemDefined())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public ExpenseCategoryModel toModel(ExpenseCategory entity) {

        if (entity == null)
            return null;

        return ExpenseCategoryModel.builder()
                .id(entity.getId())
                .categoryName(entity.getCategoryName())
                .categoryCode(entity.getCategoryCode())
                .description(entity.getDescription())
                .color(entity.getColor())
                .icon(entity.getIcon())
                .sortOrder(entity.getSortOrder())
                .systemDefined(entity.getSystemDefined())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
