package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseType;
import com.devmasters.restaurant_erp.model.Expense.ExpenseTypeModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ExpenseTypeTransformer extends Transformer<ExpenseType, ExpenseTypeModel> {

    private final OrganizationTransformer organizationTransformer;

    @Override
    public ExpenseType toEntity(ExpenseTypeModel model) {

        if (model == null)
            return null;

        return ExpenseType.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .typeName(model.getTypeName())
                .code(model.getCode())
                .description(model.getDescription())
                .requiresApproval(model.getRequiresApproval())
                .requiresAttachment(model.getRequiresAttachment())
                .taxable(model.getTaxable())
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Override
    public ExpenseTypeModel toModel(ExpenseType entity) {

        if (entity == null)
            return null;

        return ExpenseTypeModel.builder()
                .id(entity.getId())
                .typeName(entity.getTypeName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .requiresApproval(entity.getRequiresApproval())
                .requiresAttachment(entity.getRequiresAttachment())
                .taxable(entity.getTaxable())
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
