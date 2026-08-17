package com.devmasters.restaurant_erp.expense.service;

import com.devmasters.restaurant_erp.expense.domain.ExpenseApproval;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseApprovalSearchCriteria;
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