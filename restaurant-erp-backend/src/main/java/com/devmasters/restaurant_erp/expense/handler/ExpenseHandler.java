package com.devmasters.restaurant_erp.expense.handler;

import com.devmasters.restaurant_erp.expense.domain.Expense;
import com.devmasters.restaurant_erp.expense.model.ExpenseModel;
import com.devmasters.restaurant_erp.common.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseSearchCriteria;
import com.devmasters.restaurant_erp.expense.service.ExpenseService;
import com.devmasters.restaurant_erp.common.service.Sequence.CodeGeneratorService;
import com.devmasters.restaurant_erp.expense.service.ExpenseStatusService;
import com.devmasters.restaurant_erp.expense.transformer.ExpenseTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseHandler {

    private final ExpenseService service;
    private final ExpenseTransformer transformer;
    private final CodeGeneratorService codeGeneratorService;
    private final ExpenseStatusService expenseStatusService;

    public ExpenseModel create(ExpenseModel model) {

        UUID organizationId = model.getOrganizationModel().getId();

        if (model.getExpenseNo() == null ||
                model.getExpenseNo().isBlank()) {

            model.setExpenseNo(
                    codeGeneratorService.generateExpenseCode(
                            organizationId));
        }

        if (service.existsByExpenseNoAndOrganization_Id(model.getExpenseNo(), organizationId)) {
            throw new RuntimeException(
                    "Expense number already exists : "
                            + model.getExpenseNo());
        }

        calculateTotal(model);

        if (model.getReimbursable() == null) {
            model.setReimbursable(false);
        }

        if (model.getIsActive() == null) {
            model.setIsActive(true);
        }

        Expense saved = service.create(transformer.toEntity(model));

        return transformer.toModel(saved);
    }

    public PageResponse<ExpenseModel> getAll(ExpenseSearchCriteria criteria, Pageable pageable) {

        Page<Expense> page = service.search(criteria, pageable);
        return PageResponse.<ExpenseModel>builder()
                .content(transformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public ExpenseModel update(UUID id, ExpenseModel model) {

        calculateTotal(model);
        Expense updated =
                service.update(
                        id,
                        transformer.toEntity(model));
        return transformer.toModel(updated);
    }

    public ExpenseModel delete(UUID id) {
        return transformer.toModel(
                service.delete(id));
    }

    public ExpenseModel restore(UUID id) {
        return transformer.toModel(
                service.restore(id));
    }

    private void calculateTotal(
            ExpenseModel model) {

        BigDecimal subTotal = value(model.getSubTotal());
        BigDecimal tax = value(model.getTaxAmount());
        BigDecimal discount = value(model.getDiscountAmount());
        model.setTotalAmount(
                subTotal
                        .add(tax)
                        .subtract(discount));
    }

    private BigDecimal value(BigDecimal amount) {
        return amount == null
                ? BigDecimal.ZERO
                : amount;
    }
}