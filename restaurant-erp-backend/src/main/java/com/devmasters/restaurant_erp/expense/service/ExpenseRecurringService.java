package com.devmasters.restaurant_erp.expense.service;

import com.devmasters.restaurant_erp.expense.domain.ExpenseRecurring;
import com.devmasters.restaurant_erp.expense.model.searchCriteria.ExpenseRecurringSearchCriteria;
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