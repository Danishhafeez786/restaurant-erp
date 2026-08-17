package com.devmasters.restaurant_erp.expense.handler;

import com.devmasters.restaurant_erp.expense.domain.ExpenseType;
import com.devmasters.restaurant_erp.expense.model.ExpenseTypeModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseTypeSearchCriteria;
import com.devmasters.restaurant_erp.expense.service.ExpenseTypeService;
import com.devmasters.restaurant_erp.common.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.expense.transformer.ExpenseTypeTransformer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ExpenseTypeHandler {

    private final ExpenseTypeService service;
    private final ExpenseTypeTransformer transformer;
    private final CodeGeneratorService codeGeneratorService;

    public ExpenseTypeModel create(ExpenseTypeModel model) {

        UUID organizationId = model.getOrganizationModel().getId();

        if (model.getCode() == null || model.getCode().isBlank()) {

            model.setCode(codeGeneratorService.generateExpenseTypeCode(organizationId));
        }

        if (service.existsByCodeIgnoreCaseAndOrganization_Id(model.getCode(), organizationId)) {
            throw new RuntimeException(
                    "Expense Type Code already exists : "
                            + model.getCode());
        }

        if (service.existsByTypeNameIgnoreCaseAndOrganization_Id(model.getTypeName(), organizationId)) {
            throw new RuntimeException(
                    "Expense Type already exists : "
                            + model.getTypeName());
        }

        if (model.getRequiresApproval() == null)
            model.setRequiresApproval(false);

        if (model.getRequiresAttachment() == null)
            model.setRequiresAttachment(false);

        if (model.getTaxable() == null)
            model.setTaxable(false);

        if (model.getIsActive() == null)
            model.setIsActive(true);

        ExpenseType saved = service.create(transformer.toEntity(model));

        return transformer.toModel(saved);
    }

    public PageResponse<ExpenseTypeModel> getAll(ExpenseTypeSearchCriteria criteria, Pageable pageable) {

        Page<ExpenseType> page = service.search(criteria, pageable);

        return PageResponse.<ExpenseTypeModel>builder()
                .content(transformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public ExpenseTypeModel update(UUID id, ExpenseTypeModel model) {

        ExpenseType updated =
                service.update(
                        id,
                        transformer.toEntity(model));
        return transformer.toModel(updated);
    }

    public ExpenseTypeModel delete(UUID id) {

        return transformer.toModel(service.delete(id));
    }

    public ExpenseTypeModel restore(UUID id) {

        return transformer.toModel(service.restore(id));
    }
}
