package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseStatus;
import com.devmasters.restaurant_erp.model.Expense.ExpenseStatusModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseStatusSearchCriteria;
import com.devmasters.restaurant_erp.service.ExpenseStatusService;
import com.devmasters.restaurant_erp.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.transformer.ExpenseStatusTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseStatusHandler {

    private final ExpenseStatusService service;
    private final ExpenseStatusTransformer transformer;
    private final CodeGeneratorService codeGeneratorService;

    public ExpenseStatusModel create(ExpenseStatusModel model) {

        UUID organizationId = model.getOrganizationModel().getId();

        if (model.getCode() == null || model.getCode().isBlank()) {
            model.setCode(
                    codeGeneratorService.generateExpenseStatusCode(
                            organizationId));
        }

        if (service.existsByCodeIgnoreCaseAndOrganization_Id(
                model.getCode(), organizationId)) {

            throw new RuntimeException(
                    "Expense Status Code already exists : " + model.getCode());
        }

        if (service.existsByStatusNameIgnoreCaseAndOrganization_Id(
                model.getStatusName(), organizationId)) {

            throw new RuntimeException(
                    "Expense Status already exists : " + model.getStatusName());
        }

        if (Boolean.TRUE.equals(model.getDefaultStatus()) &&
                service.existsByDefaultStatusTrueAndOrganization_Id(
                        organizationId)) {

            throw new RuntimeException(
                    "A default expense status already exists.");
        }

        if (model.getDisplayOrder() == null) {
            model.setDisplayOrder(0);
        }

        if (model.getDefaultStatus() == null) {
            model.setDefaultStatus(false);
        }

        if (model.getIsActive() == null) {
            model.setIsActive(true);
        }
        return transformer.toModel(
                service.create(
                        transformer.toEntity(model)));
    }

    public PageResponse<ExpenseStatusModel> getAll(ExpenseStatusSearchCriteria criteria, Pageable pageable) {

        Page<ExpenseStatus> page = service.search(criteria, pageable);
        return PageResponse.<ExpenseStatusModel>builder()
                .content(transformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public ExpenseStatusModel update(UUID id, ExpenseStatusModel model) {

        UUID organizationId = model.getOrganizationModel().getId();

        ExpenseStatus existing = service.findById(id);

        if (!existing.getCode().equalsIgnoreCase(model.getCode()) &&
                service.existsByCodeIgnoreCaseAndOrganization_Id(
                        model.getCode(), organizationId)) {

            throw new RuntimeException(
                    "Expense Status Code already exists : " + model.getCode());
        }

        if (!existing.getStatusName().equalsIgnoreCase(model.getStatusName()) &&
                service.existsByStatusNameIgnoreCaseAndOrganization_Id(
                        model.getStatusName(), organizationId)) {

            throw new RuntimeException(
                    "Expense Status already exists : " + model.getStatusName());
        }

        if (Boolean.TRUE.equals(model.getDefaultStatus()) &&
                !Boolean.TRUE.equals(existing.getDefaultStatus()) &&
                service.existsByDefaultStatusTrueAndOrganization_IdAndIdNot(
                        organizationId, id))  {

            throw new RuntimeException(
                    "A default expense status already exists.");
        }

        return transformer.toModel(
                service.update(
                        id,
                        transformer.toEntity(model)));
    }

    public ExpenseStatusModel delete(UUID id) {
        return transformer.toModel(
                service.delete(id));
    }

    public ExpenseStatusModel restore(UUID id) {
        return transformer.toModel(
                service.restore(id));
    }
}