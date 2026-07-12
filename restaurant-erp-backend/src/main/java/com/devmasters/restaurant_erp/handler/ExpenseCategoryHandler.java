package com.devmasters.restaurant_erp.handler;


import com.devmasters.restaurant_erp.domain.Expense.ExpenseCategory;
import com.devmasters.restaurant_erp.model.Expense.ExpenseCategoryModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseCategorySearchCriteria;
import com.devmasters.restaurant_erp.service.ExpenseCategoryService;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.transformer.ExpenseCategoryTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ExpenseCategoryHandler {

    private final ExpenseCategoryService service;
    private final ExpenseCategoryTransformer transformer;
    private final CodeGeneratorService codeGeneratorService;

    public ExpenseCategoryModel create(ExpenseCategoryModel model) {

        UUID organizationId = model.getOrganizationModel().getId();

        if (model.getCategoryCode() == null || model.getCategoryCode().isBlank()) {
            model.setCategoryCode(
                    codeGeneratorService.generateExpenseCategoryCode(
                            organizationId));
        }

        if (service.existsByCategoryCodeIgnoreCaseAndOrganization_Id(model.getCategoryCode(), organizationId)) {
            throw new RuntimeException(
                    "Expense Category Code already exists : "
                            + model.getCategoryCode());
        }

        if (service.existsByCategoryNameIgnoreCaseAndOrganization_Id(model.getCategoryName(), organizationId)) {
            throw new RuntimeException(
                    "Expense Category already exists : "
                            + model.getCategoryName());
        }

        if (model.getSystemDefined() == null)
            model.setSystemDefined(false);

        if (model.getIsActive() == null)
            model.setIsActive(true);

        ExpenseCategory saved = service.create(transformer.toEntity(model));
        return transformer.toModel(saved);
    }

    public PageResponse<ExpenseCategoryModel> getAll(ExpenseCategorySearchCriteria criteria, Pageable pageable) {

        Page<ExpenseCategory> page = service.search(criteria, pageable);

        return PageResponse.<ExpenseCategoryModel>builder()
                .content(transformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public ExpenseCategoryModel update(UUID id, ExpenseCategoryModel model) {
        ExpenseCategory updated = service.update(id, transformer.toEntity(model));
        return transformer.toModel(updated);
    }

    public ExpenseCategoryModel delete(UUID id) {
        return transformer.toModel(service.delete(id));
    }

    public ExpenseCategoryModel restore(UUID id) {
        return transformer.toModel(service.restore(id));
    }
}
