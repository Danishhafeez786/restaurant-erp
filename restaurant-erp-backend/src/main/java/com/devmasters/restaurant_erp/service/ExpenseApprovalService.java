package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseApproval;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseApprovalSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ExpenseApprovalService {

    ExpenseApproval create(ExpenseApproval entity);

    Page<ExpenseApproval> search(ExpenseApprovalSearchCriteria criteria, Pageable pageable);

    ExpenseApproval findById(UUID id);

    ExpenseApproval update(UUID id, ExpenseApproval entity);

    ExpenseApproval delete(UUID id);

    ExpenseApproval restore(UUID id);
}