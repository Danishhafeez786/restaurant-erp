package com.devmasters.restaurant_erp.service;

import com.devmasters.restaurant_erp.domain.Expense.ExpenseRecurring;
import com.devmasters.restaurant_erp.model.searchcriteria.ExpenseRecurringSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ExpenseRecurringService {

    ExpenseRecurring create(ExpenseRecurring entity);

    Page<ExpenseRecurring> search(ExpenseRecurringSearchCriteria criteria, Pageable pageable);

    ExpenseRecurring findById(UUID id);

    ExpenseRecurring update(UUID id, ExpenseRecurring entity);

    ExpenseRecurring delete(UUID id);

    ExpenseRecurring restore(UUID id);
}