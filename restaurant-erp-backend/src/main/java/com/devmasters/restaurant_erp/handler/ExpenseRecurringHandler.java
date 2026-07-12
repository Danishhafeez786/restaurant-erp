package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Branch;
import com.devmasters.restaurant_erp.domain.Organization;
import com.devmasters.restaurant_erp.domain.Expense.ExpenseCategory;
import com.devmasters.restaurant_erp.domain.Expense.ExpenseRecurring;
import com.devmasters.restaurant_erp.domain.Expense.ExpenseType;
import com.devmasters.restaurant_erp.model.Expense.ExpenseRecurringModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseRecurringSearchCriteria;
import com.devmasters.restaurant_erp.repository.BranchRepository;
import com.devmasters.restaurant_erp.repository.ExpenseCategoryRepository;
import com.devmasters.restaurant_erp.repository.ExpenseTypeRepository;
import com.devmasters.restaurant_erp.repository.OrganizationRepository;
import com.devmasters.restaurant_erp.service.ExpenseRecurringService;
import com.devmasters.restaurant_erp.transformer.ExpenseRecurringTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class ExpenseRecurringHandler {

    private final ExpenseRecurringService service;
    private final ExpenseRecurringTransformer transformer;
    private final ExpenseCategoryRepository categoryRepository;
    private final ExpenseTypeRepository expenseTypeRepository;
    private final OrganizationRepository organizationRepository;
    private final BranchRepository branchRepository;


    public ExpenseRecurringModel create(ExpenseRecurringModel model) {

        validate(model);

        ExpenseRecurring entity = transformer.toEntity(model);

        if (model.getCategory() != null && model.getCategory().getId() != null) {
            ExpenseCategory category =
                    categoryRepository.findById(
                                    model.getCategory().getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Expense Category not found"));
            entity.setCategory(category);
        }

        if (model.getExpenseType() != null && model.getExpenseType().getId() != null) {
            ExpenseType type =
                    expenseTypeRepository.findById(
                                    model.getExpenseType().getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Expense Type not found"));
            entity.setExpenseType(type);
        }



        if (model.getOrganizationModel() != null && model.getOrganizationModel().getId() != null) {

            Organization organization =
                    organizationRepository.findById(
                                    model.getOrganizationModel().getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Organization not found"));
            entity.setOrganization(organization);
        }



        if (model.getBranchModel() != null && model.getBranchModel().getId() != null) {

            Branch branch =
                    branchRepository.findById(
                                    model.getBranchModel().getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Branch not found"));

            entity.setBranch(branch);
        }

        calculateNextGenerationDate(entity);


        if (entity.getAutoGenerate() == null) {
            entity.setAutoGenerate(true);
        }

        if (entity.getActive() == null) {
            entity.setActive(true);
        }

        if (entity.getIsActive() == null) {
            entity.setIsActive(true);
        }

        return transformer.toModel(service.create(entity));
    }



    public PageResponse<ExpenseRecurringModel> getAll(ExpenseRecurringSearchCriteria criteria, Pageable pageable) {

        Page<ExpenseRecurring> page =
                service.search(
                        criteria,
                        pageable);

        return PageResponse.<ExpenseRecurringModel>builder()

                .content(transformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())

                .build();
    }




    public ExpenseRecurringModel update(UUID id, ExpenseRecurringModel model) {

        ExpenseRecurring entity = transformer.toEntity(model);
        return transformer.toModel(
                service.update(
                        id,
                        entity));
    }




    public ExpenseRecurringModel delete(UUID id) {

        return transformer.toModel(
                service.delete(id));
    }

    public ExpenseRecurringModel restore(
            UUID id) {

        return transformer.toModel(
                service.restore(id));
    }




    private void validate(ExpenseRecurringModel model) {

        if (model.getTitle() == null || model.getTitle().isBlank()) {
            throw new RuntimeException(
                    "Title is required");
        }


        if (model.getAmount() == null) {
            throw new RuntimeException(
                    "Amount is required");
        }


        if (model.getFrequency() == null || model.getFrequency().isBlank()) {
            throw new RuntimeException(
                    "Frequency is required");
        }

    }




    private void calculateNextGenerationDate(ExpenseRecurring entity) {

        if (entity.getStartDate() != null) {
            entity.setNextGenerationDate(
                    entity.getStartDate());
        }
    }
}