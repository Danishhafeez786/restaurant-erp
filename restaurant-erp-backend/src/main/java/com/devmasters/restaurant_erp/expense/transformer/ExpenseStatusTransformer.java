package com.devmasters.restaurant_erp.expense.transformer;

import com.devmasters.restaurant_erp.common.transformer.Transformer;
import com.devmasters.restaurant_erp.expense.domain.ExpenseStatus;
import com.devmasters.restaurant_erp.expense.model.ExpenseStatusModel;
import com.devmasters.restaurant_erp.organization.transformer.OrganizationTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseStatusTransformer extends Transformer<ExpenseStatus, ExpenseStatusModel> {

    private final OrganizationTransformer organizationTransformer;

    @Override
    public ExpenseStatus toEntity(ExpenseStatusModel model) {

        if (model == null) {
            return null;
        }

        return ExpenseStatus.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .statusName(model.getStatusName())
                .code(model.getCode())
                .description(model.getDescription())
                .color(model.getColor())
                .displayOrder(model.getDisplayOrder())
                .defaultStatus(model.getDefaultStatus())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public ExpenseStatusModel toModel(ExpenseStatus entity) {

        if (entity == null) {
            return null;
        }

        return ExpenseStatusModel.builder()
                .id(entity.getId())
                .statusName(entity.getStatusName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .color(entity.getColor())
                .displayOrder(entity.getDisplayOrder())
                .defaultStatus(entity.getDefaultStatus())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}