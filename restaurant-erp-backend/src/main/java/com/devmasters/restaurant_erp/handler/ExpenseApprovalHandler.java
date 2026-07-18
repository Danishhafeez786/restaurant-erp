package com.devmasters.restaurant_erp.handler;

import com.devmasters.restaurant_erp.domain.Employee;
import com.devmasters.restaurant_erp.domain.Expense.Expense;
import com.devmasters.restaurant_erp.domain.Expense.ExpenseApproval;
import com.devmasters.restaurant_erp.enums.ApprovalStatus;
import com.devmasters.restaurant_erp.model.Expense.ExpenseApprovalModel;
import com.devmasters.restaurant_erp.model.pagination.PageResponse;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseApprovalSearchCriteria;
import com.devmasters.restaurant_erp.repository.EmployeeRepository;
import com.devmasters.restaurant_erp.repository.ExpenseRepository;
import com.devmasters.restaurant_erp.service.ExpenseApprovalService;
import com.devmasters.restaurant_erp.transformer.ExpenseApprovalTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExpenseApprovalHandler {

    private final ExpenseApprovalService service;
    private final ExpenseApprovalTransformer transformer;

    private final ExpenseRepository expenseRepository;
    private final EmployeeRepository employeeRepository;


    public ExpenseApprovalModel create(ExpenseApprovalModel model) {

        if (model.getExpenseId() == null) {
            throw new RuntimeException(
                    "Expense id is required.");
        }

        if (model.getApprovedById() == null) {
            throw new RuntimeException(
                    "Approver employee id is required.");
        }

        Expense expense =
                expenseRepository.findById(
                                model.getExpenseId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Expense not found."));

        Employee employee =
                employeeRepository.findById(
                                model.getApprovedById())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Approver employee not found."));


        ExpenseApproval approval = transformer.toEntity(model);
        approval.setExpense(expense);
        approval.setApprovedBy(employee);


        if (approval.getApprovalStatus() == null) {
            approval.setApprovalStatus(ApprovalStatus.PENDING);
        }

        if (approval.getApproved() == null) {
            approval.setApproved(false);
        }

        if (approval.getSubmittedAt() == null) {
            approval.setSubmittedAt(LocalDateTime.now());
        }


        if (approval.getApprovalStatus() == ApprovalStatus.APPROVED) {
            approval.setApproved(true);
            approval.setApprovedAt(LocalDateTime.now());
        }

        if (approval.getIsActive() == null) {
            approval.setIsActive(true);
        }

        return transformer.toModel(service.create(approval));
    }


    public PageResponse<ExpenseApprovalModel> getAll(ExpenseApprovalSearchCriteria criteria, Pageable pageable) {

        Page<ExpenseApproval> page = service.search(criteria, pageable);

        return PageResponse.<ExpenseApprovalModel>builder()
                .content(transformer.toModels(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }


    public ExpenseApprovalModel update(UUID id, ExpenseApprovalModel model) {

        return transformer.toModel(service.update(id, transformer.toEntity(model)));
    }


    public ExpenseApprovalModel delete(UUID id) {
        return transformer.toModel(service.delete(id));
    }


    public ExpenseApprovalModel restore(UUID id) {
        return transformer.toModel(service.restore(id));
    }
}