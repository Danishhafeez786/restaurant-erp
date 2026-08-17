package com.devmasters.restaurant_erp.expense.service;

import com.devmasters.restaurant_erp.expense.domain.Expense;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ExpenseService {

    boolean existsByExpenseNoAndOrganization_Id(String expenseNo, UUID organizationId);

    Expense create(Expense entity);

    Page<Expense> search(ExpenseSearchCriteria criteria, Pageable pageable);

    Expense findById(UUID id);

    Expense update(UUID id, Expense entity);

    Expense delete(UUID id);

    Expense restore(UUID id);
}