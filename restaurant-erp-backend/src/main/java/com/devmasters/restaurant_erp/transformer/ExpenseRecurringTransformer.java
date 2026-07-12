package com.devmasters.restaurant_erp.transformer;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseRecurring;
import com.devmasters.restaurant_erp.model.Expense.ExpenseRecurringModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseRecurringTransformer extends Transformer<ExpenseRecurring, ExpenseRecurringModel> {

    private final ExpenseCategoryTransformer categoryTransformer;
    private final ExpenseTypeTransformer expenseTypeTransformer;
    private final OrganizationTransformer organizationTransformer;
    private final BranchTransformer branchTransformer;

    @Override
    public ExpenseRecurring toEntity(ExpenseRecurringModel model) {

        if (model == null) {
            return null;
        }
        return ExpenseRecurring.builder()
                .id(model.getId() != null ? model.getId() : UUID.randomUUID())
                .title(model.getTitle())
                .amount(model.getAmount())
                .frequency(model.getFrequency())
                .intervalValue(model.getIntervalValue())
                .generateDay(model.getGenerateDay())
                .startDate(model.getStartDate())
                .endDate(model.getEndDate())
                .lastGeneratedDate(model.getLastGeneratedDate())
                .nextGenerationDate(model.getNextGenerationDate())
                .autoGenerate(model.getAutoGenerate())
                .active(model.getActive())
                .category(categoryTransformer.toEntity(model.getCategory()))
                .expenseType(expenseTypeTransformer.toEntity(model.getExpenseType()))
                .organization(organizationTransformer.toEntity(model.getOrganizationModel()))
                .branch(branchTransformer.toEntity(model.getBranchModel()))
                .isActive(model.getIsActive())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())

                .build();
    }


    @Override
    public ExpenseRecurringModel toModel(ExpenseRecurring entity) {

        if (entity == null) {
            return null;
        }

        return ExpenseRecurringModel.builder()

                .id(entity.getId())
                .title(entity.getTitle())
                .amount(entity.getAmount())
                .frequency(entity.getFrequency())
                .intervalValue(entity.getIntervalValue())
                .generateDay(entity.getGenerateDay())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .lastGeneratedDate(entity.getLastGeneratedDate())
                .nextGenerationDate(entity.getNextGenerationDate())
                .autoGenerate(entity.getAutoGenerate())
                .active(entity.getActive())
                .category(categoryTransformer.toModel(entity.getCategory()))
                .expenseType(expenseTypeTransformer.toModel(entity.getExpenseType()))
                .organizationModel(organizationTransformer.toModel(entity.getOrganization()))
                .branchModel(branchTransformer.toModel(entity.getBranch()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())

                .build();
    }
}